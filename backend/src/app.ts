import express from "express";
import dotenv from "dotenv";
import { sequelize } from "./config/database";
dotenv.config();

const app = express();
app.use(express.json());


const PORT = process.env.PORT || 3000;

sequelize.authenticate()
  .then(() => {
    console.log("Database connected");
    app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
  })
  .catch((err) => console.error("DB connection error:", err));
