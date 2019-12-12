// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import BootstrapVue from 'bootstrap-vue';
import VueApexCharts from 'vue-apexcharts';
import VueBus from 'vue-bus';

import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import '@fortawesome/fontawesome-free/css/all.css';
import '@fortawesome/fontawesome-free/js/all';

import App from './App';
import router from './router';
import store from './store';

Vue.config.productionTip = false;

Vue.use(BootstrapVue);
Vue.use(VueApexCharts);
Vue.use(VueBus);
Vue.component('apexchart', VueApexCharts);

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>',
});
