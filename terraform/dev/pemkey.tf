resource "aws_key_pair" "aws_pem_key" {
  key_name   = "abcdedu-dev-pem-key"
  public_key = var.public_key
}