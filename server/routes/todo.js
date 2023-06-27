const express = require('express');
const todoRouter = express.Router();
const User = require("../models/user");
const bcryptjs = require("bcryptjs");
//import 
const Todo = require("../models/todo")
const tokenVerify = require("../middleware/auth");
const todo = require('../models/todo');

todoRouter.post("/", tokenVerify, async (req, res, next) => {
    try {
        const toDo = await Todo.create({
            title: req.body.title,
            description: req.body.description,
            user: req.user.id
        })
        if (!toDo) {
            return res.status(400).json({
                success: false,
                msg: "Something Went Wrong"
            })
        }
        return res.status(200).json({
            success: true,
            msg: "Todo Created Successfully",
            todo: toDo
        })

    } catch (err) {
        console.log(err);
        res.status(500).json({ error: err.message });
        next(err);
    }

})

todoRouter.get("/", tokenVerify, async (req, res, next) => {
    const { finished } = req.query;
    let query = { user: req.user.id , finished: false};
    if (finished) {
        query.finished = finished;
    }
    try {
        const toDo = await Todo.find(query);
        if (!toDo) {
            return res.status(400).json({
                success: false,
                msg: "No Todo Is found",
            })

        }
        return res.status(200).json({
            success: true,
            msg: "user todo List",
            count: toDo.length,
            todo: toDo
        })
    } catch (e) {
        console.log(e);
        res.status(500).json({ error: e.message });
        next();
    }
})

todoRouter.put("/:id", tokenVerify, async (req, res, next) => {

    try {

        let toDo = await Todo.findById(req.params.id);
        if (!toDo) {
            return res.status(400).json({
                success: false,
                msg: "No Todo Is found"
            })
        }
        toDo = await Todo.findByIdAndUpdate(req.params.id, req.body, {
            new: true,
            runValidators: true
        })

        if (!toDo) {
            return res.status(400).json({
                success: false,
                msg: "Todo is Not Update"
            })
        }
        return res.status(200).json({
            success: true,
            msg: "Todo is Updated",
            todo: toDo
        })

    } catch (e) {
        console.log(e);
        res.status(500).json({ error: e.message });
        next();
    }
})

todoRouter.delete("/:id", tokenVerify, async (req, res, next) => {

    try {
        let toDo = await Todo.findById(req.params.id);
        if (!toDo) {
            return res.status(400).json({
                success: false,
                msg: "No Todo Is found"
            })
        }

        toDo = await Todo.findByIdAndDelete(req.params.id);


        if (!toDo) {
            return res.status(400).json({
                success: false,
                msg: "Todo is Not Deleted, Something went wrong!"
            })
        }

        return res.status(200).json({
            success: true,
            msg: "Todo is Deleted",
            todo: toDo
        })
    } catch (e) {
        console.log(e);
        res.status(500).json({ error: e.message });
        next();
    }

})

module.exports = todoRouter;


