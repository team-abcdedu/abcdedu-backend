chmod +x ./dev-plan.sh
terraform workspace select dev
terraform plan -var-file="dev.tfvars"
