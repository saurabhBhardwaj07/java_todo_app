const mongoose = require("mongoose");
const dotenv = require("dotenv");


dotenv.config({ path: "../config/config.env" });
const source = process.env.MONGO_URL || "mongodb+srv://saurabhkumar:%4022Kiran@cluster0.ajrhvth.mongodb.net/?retryWrites=true&w=majority";

const connectDB = async () => {
    try{
        const conn = await mongoose.connect(source);
        console.log("Connection Successfully With Mongo DB".green.underline.bold);
    } catch (e){
        console.log(e);

    }
};
module.exports = connectDB;