class Validation{

    validateEmail(email){
        var regexp = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return regexp.test(String(email).toLowerCase());
    }

    validateUsername(username){
        var regexp = /^[0-9]{6}$/;
        return regexp.test(String(username));
    }

    validatePassword(password){
        var regexp = /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,16}$/;
        return regexp.test(String(password));
    }
}

export default new Validation();
