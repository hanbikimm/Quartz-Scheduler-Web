<template>
  <div class="px-10 bg-gray-200">
    <div class="flex flex-col">

      <div class="grid grid-cols-2">
        <div class="text-4xl text-gray-600 font-medium my-8 mx-3">
          Log History
        </div>

        <div class="flex justify-end mt-9 mb-3">
          <div class="mt-4 mr-3">
            <button 
            @click="goToScheduleList()"
            class="px-5 py-2.5 font-medium tracking-wide text-white bg-indigo-600 rounded-lg hover:bg-indigo-500 focus:outline-none">
              스케줄 목록으로
            </button>
          </div>
          <div class="p-2.5 rounded-md border border-gray-200 shadow font-bold text-gray-600 bg-white text-sm mt-4">
            <input
              type="checkbox"
              class="w-4 h-5 align-bottom bg-indigo-600 rounded-md focus:ring-indigo-500"
              v-model="this.refresh.checked"
              @change="refreshList()"/>
            <input
              type="text"
              class="w-8 h-5 ml-2 text-center border border-gray-400 focus:outline-none"
              v-model="this.refresh.sec"/> 초마다 새로고침
          </div>
        </div>
      </div>
      
      <LoadingSpinner v-if="isLoading"/>
      <div v-else class="py-2 -my-1 overflow-x-auto sm:-mx-6 sm:px-6 lg:-mx-8 lg:px-8">
        <div class="inline-block min-w-full overflow-hidden align-middle border-b border-gray-200 shadow sm:rounded-lg">
          <table class="min-w-full">
            <thead>
              <tr>
                <th class="px-6 py-3 text-sm font-bold  leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                  스케줄
                </th>

                <th class="px-6 py-3 text-sm font-bold  leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                  그룹
                </th>

                <th class="px-6 py-3 text-sm font-bold  leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                  시작 시간
                </th>

                <th class="px-6 py-3 text-sm font-bold  leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                  종료 시간
                </th>

                <th class="px-6 py-3 text-sm font-bold  leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                  상태
                </th>
              </tr>
            </thead>

            <tbody class="bg-white" v-for="log in this.logs" :key="log.logId">
              <tr>
                <td class="px-6 py-4 text-sm leading-5 text-gray-500 border-b border-gray-200 whitespace-nowrap">
                  {{ log.jobName }}
                </td>

                <td class="px-6 py-4 text-sm leading-5 text-gray-500 border-b border-gray-200 whitespace-nowrap">
                  {{ log.jobGroup }}
                </td>

                <td class="px-6 py-4 text-sm leading-5 text-gray-500 border-b border-gray-200 whitespace-nowrap">
                  {{ log.startDt }}
                </td>

                <td class="px-6 py-4 text-sm leading-5 text-gray-500 border-b border-gray-200 whitespace-nowrap">
                  {{ log.endDt }}
                </td>

                <td class="px-6 py-4 border-b border-gray-200 whitespace-nowrap">
                  <div v-if="log.jobState == 'SUCCESS'"
                  class="inline-flex px-2 text-xs font-semibold leading-5 text-green-800 bg-green-100 rounded-full">
                    실행 성공
                  </div>

                  <div v-else-if="log.jobState == 'FAIL'"
                  class="inline-flex px-2 text-xs font-semibold leading-5 text-yellow-800 bg-yellow-100 rounded-full">
                    실행 실패
                  </div>

                  <div v-else-if="log.jobState == 'CREATED'"
                  class="inline-flex px-2 text-xs font-semibold leading-5 text-indigo-800 bg-indigo-100 rounded-full">
                    스케줄 생성
                  </div>

                  <div v-else-if="log.jobState == 'DELETED'"
                  class="inline-flex px-2 text-xs font-semibold leading-5 text-pink-800 bg-pink-100 rounded-full">
                    스케줄 삭제
                  </div>

                  <div v-else-if="log.jobState == 'TERMINATED'"
                  class="inline-flex px-2 text-xs font-semibold leading-5 text-black bg-gray-100 rounded-full">
                    스케줄 종료
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
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
  </div>
</template>

<script>
import SchedulerApi from '@/api/SchedulerApi.js';
import Pagination from '@/components/common/Pagination.vue';
import LoadingSpinner from '../common/LoadingSpinner.vue';

export default {
  name: 'ScheduleLog',
  components: {
    Pagination,
    LoadingSpinner,
  },
  data () {
        return {
          logs: [],

          paging: {},

          refresh: {
            sec: '30',
            checked: '',
          },

          isLoading: false,

        }
  },
  
  mounted() { 
    //mount 될 때 list 불러오기
    this.getJobLog();
  },

  methods:{
    async getJobLog(){
      try{
        this.isLoading = true;
        const results = await SchedulerApi.logList(this.paging.nowPage);
        this.isLoading = false;
        this.logs = results.result.logs;
        this.paging = results.result.paging;
      }catch(error){
        console.log(error);
      }
    },

    refreshList(){
      if(this.refresh.checked == true){
        console.log('새로고침 진행 중');
        this.repeat = setInterval(() => {
          this.getJobLog(this.paging.nowPage);
          console.log(`리스트 불러오기!`);
        }, this.refresh.sec * 1000);
      }else{
        console.log('새로고침 중지');
        clearInterval(this.repeat);
      }
    },

    goToScheduleList(){
      clearInterval(this.repeat);
      this.$router.push({ name: 'schedule list'})
    },

    // 페이징
    prevPage(){
      if(this.paging.nowPage > 1){
        this.paging.nowPage -= 1;
        this.getJobLog(this.paging.nowPage);
      }else if(this.paging.nowPage == 1){
        alert('첫번째 페이지입니다.');
      }
    },

    nextPage(){
      if(this.paging.lastPage > this.paging.nowPage){
        this.paging.nowPage += 1;
        this.getJobLog(this.paging.nowPage);
      }else if(this.paging.lastPage == this.paging.nowPage){
        alert('마지막 페이지입니다.');
      }
    },

    firstPage(){
      if(this.paging.nowPage != 1){
        this.paging.nowPage = 1;
        this.getJobLog(this.paging.nowPage);
      }else if(this.paging.nowPage == 1){
        alert('첫번째 페이지입니다.');
      }
    },

    lastPage(){
      if(this.paging.nowPage != this.paging.lastPage){
        this.paging.nowPage = this.paging.lastPage;
        this.getJobLog(this.paging.nowPage);
      }else if(this.paging.lastPage == this.paging.nowPage){
        alert('마지막 페이지입니다.');
      }
    },

    changeNowPage(page){
      if(page !== this.paging.nowPage){
        this.paging.nowPage = page;
        this.getJobLog(this.paging.nowPage);
      }
    }
  },
}
</script>