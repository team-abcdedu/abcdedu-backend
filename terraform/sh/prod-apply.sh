chmod +x ./prod-apply.sh
terraform workspace select prod
terraform apply -var-file="prod.tfvars"