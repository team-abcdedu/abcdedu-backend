chmod +x ./dev-apply.sh
terraform workspace select dev
terraform apply -var-file="dev.tfvars"
