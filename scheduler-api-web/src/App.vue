<template>
  <div class="bg-gray-200 h-screen">
      <SchedulerHeader/>
    <router-view></router-view>
  </div>
  
</template>

<script>
import SchedulerHeader from './components/common/Header.vue';
//import { mapState, mapGetters, mapMutations, mapActions } from 'vuex'

export default {
  name: 'App',
  components: {
    SchedulerHeader,

  },
  computed: {
    // ...mapState(['jobList']),
    // ...mapGetters(),
    // ...mapMutations(),
    // ...mapActions(),
  }
}
</script>

<style>

</style>
