const mongoose = require('mongoose');
const todoSchema = new mongoose.Schema({
    title: {
        type: String,
        required: true
    }
    ,
    description: {
        type: String,
        default: null
    }
    , user: {
        type: mongoose.Schema.Types.ObjectId
        , ref: "User"
    }, 
    finished: {
        type: Boolean,
        default: false
    }
    , createdAt:{
        type: Date,
        default: Date.now
    }

})



module.exports = mongoose.model("Todo" , todoSchema);