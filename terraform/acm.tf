resource "aws_acm_certificate" "ssl_certificate" {
  domain_name               = "*.abcdedu.com"
  key_algorithm             = "RSA_2048"

  options {
    certificate_transparency_logging_preference = "ENABLED"
  }
}