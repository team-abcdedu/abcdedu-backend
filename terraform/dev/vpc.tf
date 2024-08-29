# VPC 생성
resource "aws_vpc" "main" {
  cidr_block = var.vpc_cidr

  enable_dns_support = true
  enable_dns_hostnames = true

  tags = {
    Name = "main-vpc"
  }
}

# 인터넷 게이트웨이 생성
resource "aws_internet_gateway" "ig" {
  vpc_id = aws_vpc.main.id
}

# 라우팅 테이블 생성
resource "aws_route_table" "rt" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.ig.id
  }
}

# 서브넷에 라우팅 테이블 연결
resource "aws_route_table_association" "main" {
  subnet_id      = aws_subnet.subnet_a.id
  route_table_id = aws_route_table.rt.id
}

resource "aws_route_table_association" "subnet_b" {
  subnet_id      = aws_subnet.subnet_b.id
  route_table_id = aws_route_table.rt.id
}