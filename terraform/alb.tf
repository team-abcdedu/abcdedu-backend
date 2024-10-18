resource "aws_lb" "alb"{
  name = var.alb_name
  internal = false
  load_balancer_type = "application"
  security_groups = [aws_security_group.vpc.id]
  subnets = [aws_subnet.subnet_a.id, aws_subnet.subnet_b.id]
  ip_address_type         = "ipv4"
}

resource "aws_lb_target_group" "target_group" {
  name                              = var.target_group_name
  ip_address_type                   = "ipv4"
  port                              = 80
  protocol                          = "HTTP"
  protocol_version                  = "HTTP1"
  target_type                       = "instance"
  vpc_id                            = aws_vpc.main.id
  deregistration_delay              = 300
  slow_start                        = 0

  health_check {
    enabled             = true
    healthy_threshold   = 5
    interval            = 30
    matcher             = "200"
    path                = "/api/v1/actuator/health"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = 5
    unhealthy_threshold = 2
  }

  target_group_health {
    dns_failover {
      minimum_healthy_targets_count      = "1"
      minimum_healthy_targets_percentage = "off"
    }
    unhealthy_state_routing {
      minimum_healthy_targets_count      = 1
      minimum_healthy_targets_percentage = "off"
    }
  }
}

resource "aws_lb_listener" "listener" {
  load_balancer_arn = aws_lb.alb.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
  certificate_arn = aws_acm_certificate.ssl_certificate.arn
  default_action {
    type = "forward"

    forward {
      target_group {
        arn = aws_lb_target_group.target_group.arn
      }
    }
  }
}