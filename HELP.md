# Help & Troubleshooting Guide

## 🆘 Common Issues

### 1. Database Connection Refused
**Error:** `Connection to localhost:5432 refused`
**Solution:**
- Ensure PostgreSQL is running.
- Verify `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` in your `.env` file or environment variables.
- Check if the database `mydeseret_db` exists.

### 2. Flyway Migration Failed
**Error:** `FlywayValidateException: Validate failed: Migrations have failed validation`
**Solution:**
- This usually happens if the database schema is out of sync with the migration scripts.
- **Fix:** Enable out-of-order migrations in `application.properties`:
  ```properties
  spring.flyway.out-of-order=true
  ```
- Or, manually repair the `flyway_schema_history` table in the database.

### 3. RabbitMQ Connection Failed
**Error:** `AmqpConnectException: java.net.ConnectException: Connection refused`
**Solution:**
- Ensure RabbitMQ is running (e.g., via Docker: `docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management`).
- Verify `SPRING_RABBITMQ_HOST` and `SPRING_RABBITMQ_PORT`.

### 4. JWT Token Issues
**Error:** `401 Unauthorized`
**Solution:**
- Ensure you are sending the header `Authorization: Bearer <YOUR_TOKEN>`.
- Check if the token has expired.
- If you restarted the server and are using an in-memory secret (not recommended for prod), old tokens might be invalid. Ensure `JWT_SECRET` is consistent.

## ❓ FAQ

### Q: How do I reset the database?
**A:** You can drop the `public` schema and restart the application. Flyway will recreate everything.
```sql
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
```

### Q: How do I enable 2FA?
**A:** Call `POST /api/v1/auth/2fa/setup` to get a QR code. Scan it with Google Authenticator. Then verify with `POST /api/v1/auth/2fa/verify`.

### Q: Where are the logs?
**A:** Logs are output to the console (stdout). In a production environment, they should be piped to a file or a logging service (like ELK or CloudWatch).

## 📞 Support

For further assistance, please contact the development team or open an issue on the GitHub repository.
