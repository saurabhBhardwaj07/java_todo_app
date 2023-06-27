
//imports from packages
const express = require('express');
const colors = require('colors');
const cors = require("cors");
const dotenv = require("dotenv");


//imports from other files
const connectDB = require("../config/db");

//imports Router ----------
const authRouter = require("../routes/auth");
const todoRouter = require("../routes/todo")



dotenv.config({ path: "../config/config.env" });
// get port from the config file
const PORT = process.env.PORT || 3000;
const app = express();




const corsOptions = {
  origin: '*',
};



// middleware
// Client -> Server -> Client 
app.use(express.json());
app.use(express.urlencoded({
  extended: true
}));
app.use(cors(corsOptions));
app.use('/api', authRouter);
app.use('/todo', todoRouter);




// Connection
connectDB();


app.listen(PORT, () => console.log(`Connected to the server ${PORT}!`.red.underline.bold));