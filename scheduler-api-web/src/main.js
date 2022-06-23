import { createApp } from 'vue'
import App from './App.vue'
import router from './assets/Router.js';
import './assets/tailwind.css';
import store from './stores/Stores.js';

import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';


 
//createApp(App).use(router).mount('#app')
const app = createApp(App);
app.use(router);
app.use(store);
app.component('Datepicker', Datepicker);
app.mount('#app');
