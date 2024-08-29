# Elastic IP 생성
resource "aws_eip" "web_eip" {
  instance = aws_instance.ec2.id
}