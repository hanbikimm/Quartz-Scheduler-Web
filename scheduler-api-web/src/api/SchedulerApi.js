import { createSchedulerInstance } from "./Interceptors";

const instance = createSchedulerInstance();

class SchedulerApi{
    URL = '/api/scheduler'

    scheduleList(nowPage){
        return instance.get(this.URL + '/jobs', {params: {nowPage: nowPage}})
                .then((response)=>response.data);
    }

    scheduleDetail(id){
        return instance.get(this.URL + `/jobs/${id}`)
                .then((response)=>response.data);
    }

    logList(nowPage){
        return instance.get(this.URL + '/logs', {params: {nowPage: nowPage}})
                .then((response)=>response.data);
    }

    scheduleCreate(job){
        return instance.post(this.URL + '/jobs', {...job})
                .then((response)=>response.data)
    }

    scheduleUpdate(id, job){
        return instance.put(this.URL + `/jobs/${id}`, {...job})
                .then((response)=>response.data)
    }

    scheduleDelete(id){
        return instance.delete(this.URL + `/jobs/${id}`)
                .then((response)=>response.data)
    }

    stateUpdate(id, jobState){
        return instance.put(this.URL + `/jobs/${id}/state`, {jobState})
                .then((response)=>response.data)
    }

   
}

export default new SchedulerApi();