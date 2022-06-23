import { createMemberInstance } from "./Interceptors";

const instance = createMemberInstance();

class MemberApi{
    URL = '/api/members'

    memberCreate(member){
        return instance.post(this.URL + '/register', {...member})
                .then((response)=>response.data)
                .catch(error => error.response.data);
    }

    memberSignin(member){
        return instance.post(this.URL + '/login', {...member})
                .then((response)=>response.data)
                .catch(error => error.response.data);
    }

    checkUsername(username){
        return instance.get(this.URL + '/username-check', {params: {username: username}})
                .then((response)=>response.data)
                .catch(error => error.response.data);
    }

    checkEmail(email){
        return instance.get(this.URL + '/email-check', {params: {email: email}})
                .then((response)=>response.data)
                .catch(error => error.response.data);
    }
 
}

export default new MemberApi();