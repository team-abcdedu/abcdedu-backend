resource "aws_elasticache_cluster" "redis" {
  cluster_id           = "abcdedu-redis-cluster1-${var.environment}"
  node_type            = "cache.t3.micro"
  engine               = "redis"
  engine_version       = "7.0"
  num_cache_nodes      = 1     # 노드 수
  port                 = 6379  # Redis 기본 포트
  subnet_group_name    = aws_elasticache_subnet_group.redis_subnet_group.name
  security_group_ids   = [aws_security_group.redis_sg.id]

  tags = {
    Name = "abcdedu-redis-${var.environment}"
  }
}

resource "aws_elasticache_subnet_group" "redis_subnet_group" {
  name       = "my-redis-subnet-group"
  subnet_ids = [aws_subnet.subnet_a.id, aws_subnet.subnet_b.id]

  tags = {
    Name = "abcdedu-redis-subnet-group-${var.environment}"
  }
}
