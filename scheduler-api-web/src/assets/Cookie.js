import Cookies from "universal-cookie";

const cookies = new Cookies();


class Cookie{

    setCookie(name, value) {
        return cookies.set(name, value, {
            path : '/',
            maxAge: 6 * 60* 60,
            secure: true,
            // httpOnly: true
        });
    }
      
    getCookie(name) {
        return cookies.get(name);
    }
    
    removeCookie(name) {
        return cookies.remove(name, {path : '/'});
    }
      
}


export default new Cookie();