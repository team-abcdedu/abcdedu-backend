resource "aws_instance" "ec2" {
  ami                         = var.ami_id
  instance_type               = var.instance_type
  subnet_id                   = aws_subnet.subnet_a.id
  vpc_security_group_ids = [aws_security_group.vpc.id]
  associate_public_ip_address = true
  key_name                    = aws_key_pair.aws_pem_key.key_name
  lifecycle {
    prevent_destroy = true
  }
  tags = {
    Name = "abcdedu-ec2-${var.environment}"
  }
}