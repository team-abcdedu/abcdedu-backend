variable "region" {
  description = "AWS region to deploy resources"
  default     = "ap-northeast-2"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  default     = "10.0.0.0/16"
}

variable "instance_type" {
  description = "EC2 instance type"
  default     = "t2.micro"
}

variable "ami_id" {
  description = "AMI ID for the EC2 instance"
  type        = string
}

variable "db_password" {
  description = "The password for the RDS instance"
  type        = string
}
variable "s3_policy_arn" {
  description = "s3 policy arn"
  type = string
}

variable "db_username" {
  description = "The username for the RDS instance"
  type        = string
}

variable "public_key" {
  description = "The public key for the AWS key pair"
  type        = string
}

variable "aws_access_key" {
  description = "access Key"
  type = string
}

variable "aws_secret_key" {
  description = "secret access Key"
  type = string
}

