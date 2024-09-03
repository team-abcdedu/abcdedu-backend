chmod +x ./prod-plan.sh
terraform workspace select prod
terraform plan -var-file="prod.tfvars"