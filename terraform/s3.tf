resource "aws_s3_bucket" "s3_dev" {
  bucket = "abcdedu-s3-${var.environment}"

  tags = {
    Name = "abcdedu-bucket-${var.environment}"
  }
}

resource "aws_s3_bucket_public_access_block" "public-access" {
  bucket = aws_s3_bucket.s3_dev.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

data "aws_iam_policy_document" "s3-policy" {
  statement {
    # 퍼블릭 읽기 접근 허용
    principals {
      type        = "AWS"
      identifiers = ["*"]
    }

    actions = [
      "s3:GetObject",
    ]

    resources = [
      "${aws_s3_bucket.s3_dev.arn}",
      "${aws_s3_bucket.s3_dev.arn}/*",
    ]

    effect = "Allow"
  }

  statement {
    # 특정 사용자 업로드 권한 허용
    principals {
      type        = "AWS"
      identifiers = [var.s3_policy_arn]
    }

    actions = [
      "s3:PutObject",
    ]

    resources = [
      "${aws_s3_bucket.s3_dev.arn}",
      "${aws_s3_bucket.s3_dev.arn}/*",
    ]

    effect = "Allow"
  }
}
resource "aws_s3_bucket_policy" "combined_policy" {
  bucket = aws_s3_bucket.s3_dev.id
  policy = data.aws_iam_policy_document.s3-policy.json
}