import { createStore } from "vuex";
import createPersistedState from 'vuex-persistedstate';
import { Member } from "@/stores/modules/MemberStore.js";
import { Schedule } from "@/stores/modules/ScheduleStore.js";


export default createStore({
    modules: {
        Member,
        Schedule,
    },
    plugins: [
        createPersistedState({
            paths: ['Member'],
        })
    ]
});