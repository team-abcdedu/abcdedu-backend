resource "aws_key_pair" "aws_pem_key" {
  key_name   = "abcdedu-${var.environment}-pem-key"
  public_key = var.public_key
}