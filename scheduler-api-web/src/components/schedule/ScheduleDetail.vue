<template>
  <div class="mt-4 px-10 py-7 bg-gray-200">
    <LoadingSpinner v-if="this.isLoading"/>
    <div v-else class="p-6 bg-white rounded-md shadow-md">
      <div class="flex flex-col mt-3 mr-3 justify-end items-end text-sm text-black">
        <div class="cursor-pointer" @click="goToScheduleList()">
          <svg
            class="text-black fill-current"
            xmlns="http://www.w3.org/2000/svg"
            width="18"
            height="18"
            viewBox="0 0 18 18">
            <path
              d="M14.53 4.53l-1.06-1.06L9 7.94 4.53 3.47 3.47 4.53 7.94 9l-4.47 4.47 1.06 1.06L9 10.06l4.47 4.47 1.06-1.06L10.06 9z" />
          </svg>
        </div>
      </div>

      <div class="grid grid-cols-1 gap-6 sm:grid-cols-2">
        <div class="text-gray-700 text-3xl ml-2 mb-4">
              {{ job.jobName }}
        </div>
        
        <div></div>

        <div>
          <label class="text-gray-700 ml-1">
              그룹
          </label>
          <input
            class="block w-full p-2 my-1 border border-gray-200 rounded-md focus:outline-none focus:border-gray-400"
            type="text"
            :disabled="true"
            v-model="job.jobGroup"/>
        </div>

        <div>
          <label class="text-gray-700 ml-1">
              생성자
          </label>
          <input
            class="block w-full p-2 my-1 border border-gray-200 rounded-md focus:outline-none focus:border-gray-400"
            type="text"
            :disabled="true"
            v-model="job.name"/>
        </div>

        <div>
          <label class="text-gray-700 ml-1">
              시작일
          </label>
          <input
            class="block w-full p-2 my-1 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
            type="text"
            v-model="job.startDt"/>
        </div>

        <div>
          <label class="text-gray-700 ml-1">
              종료일
          </label>
          <input
            class="block w-full p-2 my-1 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
            type="text"
            v-model="job.endDt"/>
        </div>

        <div>
          <label class="text-gray-700 ml-1">
              최근 실행 시간
          </label>
          <input
            class="block w-full p-2 my-1 border border-gray-200 rounded-md focus:outline-none focus:border-gray-400"
            type="text"
            :disabled="true"
            v-model="job.prevFireDt"/>
        </div>

        <div>
          <label class="text-gray-700 ml-1">
              다음 실행 시간
          </label>
          <input
            class="block w-full p-2 my-1 border border-gray-200 rounded-md focus:outline-none focus:border-gray-400"
            type="text"
            :disabled="true"
            v-model="job.nextFireDt"/>
        </div>

        <div>
          <label class="text-gray-700 ml-1">
              상태
          </label>
          <button 
          v-if="job.jobState == 'SCHEDULED'"
          @click="updateState()"
          class="text-sm ml-3 px-2 py-0.5 rounded-md mx-1 border bg-indigo-100 border-gray-200">
            정지
          </button>
          <button 
          v-if="job.jobState == 'PAUSED'"
          @click="updateState()"
          class="text-sm ml-3 px-2 py-0.5 rounded-md mx-1 border bg-indigo-100 border-gray-200">
            실행
          </button>
          <input
            class="block w-full p-2 my-1 border border-gray-200 rounded-md focus:outline-none focus:border-gray-400"
            type="text"
            :disabled="true"
            v-model="job.jobState"/>
        </div>
        
        <div>
          <label class="text-gray-700 ml-1">
              클래스 이름
          </label>
          <input
            class="block w-full p-2 my-1 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
            type="text"
            v-model="job.jobClass"/>
        </div>

        <div>
          <label class="text-gray-700 ml-1">
              트리거 타입
          </label>
          <input
            class="block w-full p-2 my-1 border border-gray-200 rounded-md focus:outline-none focus:border-gray-400"
            type="text"
            :disabled="true"
            v-model="job.trigger"/>
        </div>

        <div v-if="job.trigger == 'CRON'">
          <label class="text-gray-700 ml-1">
              표현식
          </label>
          <input
            class="block w-full p-2 my-1 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
            type="text"
            v-model="job.cronExpression"/>
        </div>

        <div v-if="job.trigger == 'SIMPLE'">
          <label class="text-gray-700 ml-1">
              반복 간격
          </label>
          <input
            class="block w-full p-2 my-1 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
            type="text"
            v-model="job.repeatInterval"/>
        </div>

        <div v-if="job.trigger == 'SIMPLE'">
          <label class="text-gray-700 ml-1">
              반복 횟수
          </label>
          <input
            class="block w-full p-2 my-1 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
            type="text"
            v-model="job.repeatCount"/>
        </div>
        
        <div>
          <label class="text-gray-700 ml-1">
              설명 (선택)
          </label>
          <input
            class="block w-full p-2 my-1 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
            type="text"
            v-model="job.jobDesc"/>
        </div>
      </div>


      <div class="flex justify-end pt-7">
        <button
          class="px-6 py-3 font-medium tracking-wide text-white bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none"
          @click="itemsCheck()">
          수정
        </button>
        <button
          class="px-6 py-3 mx-2 font-medium tracking-wide text-white bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none"
          @click="deleteJob()">
          삭제
        </button>
      </div>
    </div>

    <div v-if="isLoading"></div>
    <div v-else class="flex flex-col mt-8">
        <div class="py-2 -my-2 overflow-x-auto sm:-mx-6 sm:px-6 lg:-mx-8 lg:px-8">
          <div class="inline-block min-w-full overflow-hidden align-middle border-b border-gray-200 shadow sm:rounded-lg">
              <table class="min-w-full">
                  <thead>
                      <tr>
                          <th class="px-6 py-3 text-sm font-bold  leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                          로그 시간
                          </th>
                          <th class="px-6 py-3 text-sm font-bold  leading-4 tracking-wider text-left text-gray-500 uppercase border-b border-gray-200 bg-gray-50">
                          로그 메세지
                          </th>
                      </tr>
                  </thead>
                  <tbody class="bg-white">
                      <tr v-for="log in this.job.indivLogResponse" :key="log.loggingDt">
                          <td class="px-6 py-4 text-sm leading-5 text-gray-500 border-b border-gray-200 whitespace-nowrap">
                              {{ log.loggingDt}}
                          </td>

                          <td class="px-6 py-4 text-sm leading-5 text-gray-500 border-b border-gray-200 whitespace-nowrap">
                              {{ log.message}}
                          </td>
                      </tr>
                  </tbody>
              </table>
          </div>
        </div>
    </div>
  </div>
</template>

<script>
import SchedulerApi from '@/api/SchedulerApi.js';
import LoadingSpinner from '../common/LoadingSpinner.vue';
export default {
  components: { LoadingSpinner },
  name: 'ScheduleDetail',

  data(){

    return{
      job: {
        //공통
        jobName : '',
        jobGroup : '',
        jobClass: '',
        startDt: '',
        endDt: '',
        jobDesc: '',
        trigger: '',
        jobState: '',
        indivLogResponse: [],

        //cron job
        cronExpression: '',

        //simple job
        repeatInterval: '',
        repeatCount: '',
      },

      isLoading: false,


    }
  },

  mounted(){
    this.getJobDetail();
  },
  

  methods:{
    async getJobDetail(){
      try{
        this.isLoading = true;
        const data = await SchedulerApi.scheduleDetail(this.$route.params.id);
        this.isLoading = false;
        this.job = data.result;
      }catch(error){
        console.log(error);
      }
    },

    itemsCheck(){
      if(this.isNull()){
        if(this.job.trigger == 'SIMPLE'){
          if(this.job.repeatInterval <= 0 || this.job.repeatCount <= 0){
          alert('반복 간격 및 횟수는 0보다 큰 정수를 입력해주세요!');
          }else{
            this.updateJob();
          }
        }else{
          this.updateJob();
        }
      }else{
        alert(`필수 입력 항목을 다 작성했는지 확인해주시고, 다시 시도해주세요!`);
      }
    },

    async updateJob(){
      try{
        const data = await SchedulerApi.scheduleUpdate(this.$route.params.id, this.job);
        if(data.success == true){
          alert("스케줄 수정이 완료되었습니다!");
          this.$router.go();
        }
      }catch(error){
        console.log(error);
      }
      
    },

    async deleteJob(){
      try{
        if(confirm("스케줄을 삭제하시겠습니까?")){
          await SchedulerApi.scheduleDelete(this.$route.params.id);
          alert("스케줄이 성공적으로 삭제되었습니다!");
          this.$router.push({ name: "schedule list" });
        } 
      }catch(error){
        console.log(error);
      }
    },

    async updateState(){
      try {
        if(this.job.jobState == 'SCHEDULED'){
          this.job.jobState = 'PAUSED';
        }else if(this.job.jobState == 'PAUSED'){
          this.job.jobState = 'RESUMED';
        }
        await SchedulerApi.stateUpdate(this.$route.params.id, this.job.jobState);
        alert('스케줄 상태가 수정되었습니다!');
        this.$router.go();
      }catch(error){
        console.log(error);
      }
    },

    isNull(){
      if(this.job.startDt != '' && this.job.endDt != '' && this.job.jobClass != ''){
        if(this.job.trigger == 'SIMPLE'){
          if(this.job.repeatInterval == '' || this.job.repeatCount == ''){
            return false;
          }
        }else if(this.job.trigger == 'CRON'){
          if(this.job.cronExpression == ''){
            return false;
          }
        }
      }else{
        return false;
      }
      return true;
    },

    goToScheduleList(){
      this.$router.push({ name: 'schedule list'})
    }

  }

}
</script>

<style>

</style>