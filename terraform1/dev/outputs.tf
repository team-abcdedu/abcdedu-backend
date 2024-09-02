output "vpc_id" {
  description = "The ID of the VPC"
  value       = aws_vpc.main.id
}

output "subnetA_id" {
  description = "The ID of the Subnet"
  value       = aws_subnet.subnet_a.id
}
output "subnetB_id" {
  description = "The ID of the Subnet"
  value       = aws_subnet.subnet_b.id
}

output "security_group_id" {
  description = "The ID of the Security Group"
  value       = aws_security_group.vpc.id
}

output "elastic_ip" {
  description = "The Elastic IP associated with the web instance"
  value       = aws_eip.web_eip.public_ip
}

output "redis_endpoint" {
  description = "The DNS endpoint of the Redis cluster"
  value       = aws_elasticache_cluster.redis.cache_nodes[0].address
}

output "rds_endpoint" {
  description = "The DNS endpoint of the RDS"
  value       = aws_db_instance.abcdedu_db.address
}

output "ecr_repository_uri" {
  description = "The URI of the ECR repository"
  value       = aws_ecr_repository.ecr.repository_url
}