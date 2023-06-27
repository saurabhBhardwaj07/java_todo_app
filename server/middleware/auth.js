const jwt = require("jsonwebtoken");
const tokenVerify = async (req , res , next) => {
   try{
    const token = req.header("Authorization");
    if(!token)
    return res.status(401).json({msg: "No auth token, access denied"});
    const verified = jwt.verify(token, "passwordKey");
    if (!verified) return res.status(401). json({msg: "Auth Token Is Not Verified"});
    req.user = verified.id;
    req.token = token;
    next();
   } catch (e){
    res.status(500).json({ error: e.message });
   }
}

module.exports = tokenVerify ;