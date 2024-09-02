# RDS 인스턴스 생성
resource "aws_db_instance" "abcdedu_db" {
  identifier        = "abcdedurdsdev"
  instance_class    = "db.t3.micro"
  engine            = "mysql"
  engine_version    = "8.0"
  username          = var.db_username
  password          = var.db_password
  db_name           = "abcdeduDev"
  allocated_storage = 20
  storage_type      = "gp2"
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  db_subnet_group_name   = aws_db_subnet_group.rds_subnet_group.name
  backup_retention_period = 7
  publicly_accessible    = true

  tags = {
    Name = "my-db-instance"
  }
}

# DB Subnet Group 생성
resource "aws_db_subnet_group" "rds_subnet_group" {
  name       = "main-db-subnet-group"
  subnet_ids = [aws_subnet.subnet_a.id,aws_subnet.subnet_b.id]

  tags = {
    Name = "main-db-subnet-group"
  }
}
