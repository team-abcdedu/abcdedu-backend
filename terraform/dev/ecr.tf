resource "aws_ecr_repository" "ecr" {
  name                 = "abcdedu-server-ecr-dev"
  image_tag_mutability = "MUTABLE"

  tags = {
    Name = "abcdedu-ecr-dev"
  }
}