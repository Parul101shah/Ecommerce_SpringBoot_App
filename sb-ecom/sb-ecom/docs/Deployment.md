# 🚀 EcomStore — AWS Deployment Proof

This document contains evidence of the EcomStore Spring Boot application being deployed and running on AWS.

---

## ☁️ AWS Infrastructure

### EC2 Instance

The Spring Boot application is deployed on an Amazon EC2 instance running Amazon Linux 2023.



### EC2 Public IP

The deployed application is accessible through the EC2 public IP address.

---

## 🔐 EC2 Connection

SSH connection to the Amazon Linux EC2 instance was successfully established.

---

## 🐳 Docker Deployment

Docker is used to containerize and run the application infrastructure.

Docker Compose manages the application containers.

```bash
docker compose -f docker-compose.aws.yml up --build -d
