resource "aws_ecr_repository" "ecr" {
  name                 = "abcdedu-server-ecr-${var.environment}"
  image_tag_mutability = "MUTABLE"

  tags = {
    Name = "abcdedu-ecr-${var.environment}"
  }
}