<template>
  <div class="px-10 py-6 bg-gray-200">
      <div class="flex flex-wrap">

          <!-- 박스1 -->
        <div class="w-full px-6 sm:w-1/2 xl:w-1/4">
          <div class="flex items-center px-5 py-6 bg-white border border-gray-200 rounded-md shadow-sm">
            
            <div class="p-3 bg-indigo-600 bg-opacity-75 rounded-full">
            </div>

            <div class="mx-5">
              <h4 class="text-2xl font-semibold text-gray-700">{{ this.nums.numOfJobs }}</h4>
              <div class="text-gray-500">스케줄 종류</div>
            </div>
          </div>
        </div>

        <!-- 박스2 -->
        <div class="w-full px-6 mt-6 sm:w-1/2 xl:w-1/4 sm:mt-0">
          <div class="flex items-center px-5 py-6 bg-white border border-gray-200 rounded-md shadow-sm">
            <div class="p-3 bg-blue-600 bg-opacity-75 rounded-full">
            </div>

            <div class="mx-5">
              <h4 class="text-2xl font-semibold text-gray-700">{{ this.nums.numOfGroups }}</h4>
              <div class="text-gray-500">그룹 종류</div>
            </div>
          </div>
        </div>

        <!-- 박스3 -->
        <div class="w-full px-6 mt-6 sm:w-1/2 xl:w-1/4 xl:mt-0">
          <div class="flex items-center px-5 py-6 bg-white border border-gray-200 rounded-md shadow-sm">
            <div class="p-3 bg-green-600 bg-opacity-75 rounded-full">
            </div>

            <div class="mx-5">
              <h4 class="text-2xl font-semibold text-gray-700">{{ this.nums.numOfRunningJobs }}</h4>
              <div class="text-gray-500">실행 중인 스케줄</div>
            </div>
          </div>
        </div>

        <!-- 박스4 -->
        <div class="w-full px-6 mt-6 sm:w-1/2 xl:w-1/4 xl:mt-0">
          <div class="flex items-center px-5 py-6 bg-white border border-gray-200 rounded-md shadow-sm">
            <div class="p-3 bg-pink-600 bg-opacity-75 rounded-full">
            </div>

            <div class="mx-5">
              <h4 class="text-2xl font-semibold text-gray-700">{{ this.nums.numOfPausedJobs }}</h4>
              <div class="text-gray-500">정지된 스케줄</div>
            </div>
          </div>
        </div>
      </div>


    <div class="mx-3 grid grid-cols-3">
      <div class="col-span-2 flex justify-start mt-9">
        <ScheduleAdd/>
        <div>
          <button
            @click="goToLog()"
            class="px-6 py-3 ml-4 text-indigo-500 bg-white rounded-lg hover:bg-gray-100 hover:text-indigo-400 focus:outline-none">
            로그 히스토리
          </button>
        </div>
      </div>

      <div class="flex justify-end mt-9">
        <div class="p-2.5 rounded-md border border-gray-200 shadow font-bold text-gray-600 bg-white text-sm mt-2">
          <input
            type="checkbox"
            class="w-4 h-5 align-bottom bg-indigo-600 rounded-md focus:ring-indigo-500"
            v-model="this.refresh.checked"
            @change="refreshList()"/>
          <input
            type="text"
            class="w-8 h-5 ml-2 text-center border border-gray-300 focus:outline-none"
            v-model="this.refresh.sec"/> 초마다 새로고침
        </div>
      </div>

    </div>

    <LoadingSpinner v-if="this.isLoading"/>

    <div class="flex flex-col mt-7" v-else>
      <div class="py-2 -my-2 overflow-x-auto sm:-mx-6 sm:px-6 lg:-mx-8 lg:px-8">
          <div class="inline-block min-w-full overflow-hidden align-middle border-b border-gray-200 shadow sm:rounded-lg">
              <table class="min-w-full">
                  <thead>
                      <tr>
                          <th class="px-6 py-3 text-sm font-bold leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                          생성자
                          </th>
                          <th class="px-6 py-3 text-sm font-bold leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                          스케줄
                          </th>
                          <th class="px-6 py-3 text-sm font-bold leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                          그룹
                          </th>
                          <th class="px-6 py-3 text-sm font-bold leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                          최근 실행 시간
                          </th>
                          <th class="px-6 py-3 text-sm font-bold leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                          다음 실행 시간
                          </th>
                          <th class="px-6 py-3 text-sm font-bold leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                          상태
                          </th>
                      </tr>
                  </thead>

                  <ScheduleListItem 
                    v-for="job in this.jobs" :key="job.labelId"
                    :job = "job"
                    @click="goToJobDetail(job.labelId)"/>

              </table>
          </div>
      </div>
    </div>

    <div v-if="isLoading"></div>
    <div v-else>
      <Pagination
        :paging="paging"
        v-on:prevPage="prevPage"
        v-on:nextPage="nextPage"
        v-on:firstPage="firstPage"
        v-on:lastPage="lastPage"
        v-on:changeNowPage="changeNowPage"/>
    </div>
    

  </div>
</template>

<script>
import ScheduleAdd from '@/components/schedule/ScheduleAdd.vue';
import ScheduleListItem from '@/components/schedule/ScheduleListItem.vue';
import Pagination from '@/components/common/Pagination.vue';
import SchedulerApi from '@/api/SchedulerApi.js';
import LoadingSpinner from '../common/LoadingSpinner.vue';


export default {
  name: 'ScheduleList',
  components: {
    ScheduleAdd,
    ScheduleListItem,
    Pagination,
    LoadingSpinner,
  },

  data () {
    return {
      refresh: {
        sec: '30',
        checked: '',
      },

      isLoading: false,

      repeat: '',

      jobs: [],

      nums: {},

      paging: {},
    }
  },
  
  mounted() { 
    //mount 될 때 list 불러오기
    this.getJobList();

  },

  methods:{
    async getJobList(){
      try{
        this.isLoading = true;
        const results = await SchedulerApi.scheduleList(this.paging.nowPage);
        this.isLoading = false;
        this.nums = results.result.nums;
        this.jobs = results.result.jobs;
        this.paging = results.result.paging;
      }catch(error){
        console.log(error);
      }
    },

    goToJobDetail(id){
      clearInterval(this.repeat);
      this.$router.push({
      name: 'schedule detail',
      params: { id: id }
      })
    },
    
    refreshList(){
      if(this.refresh.checked == true){
        console.log('새로고침 진행 중');
        this.repeat = setInterval(() => {
          this.getJobList(this.paging.nowPage);
          console.log(`리스트 불러오기!`);
        }, this.refresh.sec * 1000);
      }else{
        console.log('새로고침 중지');
        clearInterval(this.repeat);
      }
    },

    goToLog(){
      clearInterval(this.repeat);
      this.$router.push({ name: 'schedule log'});
    },

    // 페이징
    prevPage(){
      if(this.paging.nowPage > 1){
        this.paging.nowPage -= 1;
        this.getJobList(this.paging.nowPage);
      }else if(this.paging.nowPage == 1){
        alert('첫번째 페이지입니다.');
      }
    },

    nextPage(){
      if(this.paging.lastPage > this.paging.nowPage){
        this.paging.nowPage += 1;
        this.getJobList(this.paging.nowPage);
      }else if(this.paging.lastPage == this.paging.nowPage){
        alert('마지막 페이지입니다.');
      }
    },

    firstPage(){
      if(this.paging.nowPage != 1){
        this.paging.nowPage = 1;
        this.getJobList(this.paging.nowPage);
      }else if(this.paging.nowPage == 1){
        alert('첫번째 페이지입니다.');
      }
    },

    lastPage(){
      if(this.paging.nowPage != this.paging.lastPage){
        this.paging.nowPage = this.paging.lastPage;
        this.getJobList(this.paging.nowPage);
      }else if(this.paging.lastPage == this.paging.nowPage){
        alert('마지막 페이지입니다.');
      }
    },

    changeNowPage(page){
      if(page !== this.paging.nowPage){
        this.paging.nowPage = page;
        this.getJobList(this.paging.nowPage);
      }
    }

  },

}
</script>