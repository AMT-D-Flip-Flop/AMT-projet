/**
 * Date de création     : 15.11.2021
 * Groupe               : AMT-D-Flip-Flop
 * Description          : Authentication server to work locally
 * Remarque             :
 * Source :
 * https://expressjs.com/en/resources/middleware/body-parser.html
 * https://stackoverflow.com/questions/10005939/how-do-i-consume-the-json-post-data-in-an-express-application
 * https://expressjs.com/fr/starter/hello-world.html
 */


const express = require('express');
const app = express();
const port = 3000;
//var  bodyParser = require('body-parser');
var bodyParser = require('body-parser')
class User{
    constructor(username, password){
        this.username = username;
        this.password = password;
    }
}
var users = [];

//app.use(bodyParser.json());
/*

 */
//app.use(express.json());
var jsonParser = bodyParser.json()
app.get('/', (req, res) => {
    res.send('Hello World!')
})

// With middleware
/*app.use('/accounts/register', function(req, res, next){
    users.push(req.body)
    console.log(users)
    res.json({
        "id": 0,
        "username": "test",
        "role": "admin"
    })
    next();
})*/
const jwt = require('jsonwebtoken');

app.post('/auth/login', jsonParser, function (
    req, res) {
    token = ""

    let send;
    let user;
    if (req.body.username === "test" && req.body.password === "test") {


        user = {
            "account": {
                "id": 0,
                "username": "string",
                "role": "string"
            }
        }
        const token = jwt.sign(user, 'secret',{ algorithm: 'HS256'});
        console.log("succs", token)
        send = {
            "token": token,
            "account": {
                "id": 0,
                "username": "string",
                "role": "string"
            }
        }
    } else {
        token = {
            "error": "string"
        }
        console.log("succs", token)
    }
    res.json(send)
})
app.post('/accounts/register', jsonParser, function (
    req, res) {
    console.log(req.body)
    users.push(req.body)

    res.json({
        "username": req.body.username,
        "role": "admin"
    })
})

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
})
const passport = require('passport');
var JwtStrategy = require('passport-jwt').Strategy,
    ExtractJwt = require('passport-jwt').ExtractJwt;
var opts = {}
opts.jwtFromRequest = ExtractJwt.fromAuthHeaderAsBearerToken();
opts.secretOrKey = 'secret';
opts.issuer = 'accounts.examplesoft.com';
opts.audience = 'yoursite.net';
opts.algorithms = ["HS256"]
passport.use(new JwtStrategy(opts, function(jwt_payload, done) {
    User.findOne({id: jwt_payload.sub}, function(err, user) {
        if (err) {
            return done(err, false);
        }
        if (user) {
            return done(null, user);
        } else {
            return done(null, false);
            // or you could create a new account
        }
    });
}));
