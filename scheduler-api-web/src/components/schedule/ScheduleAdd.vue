<template>
  <div>
      <div>
        <button
          @click="open = true"
          class="px-6 py-3 font-medium tracking-wide text-white bg-indigo-600 rounded-lg hover:bg-indigo-500 focus:outline-none">
          스케줄 등록
        </button>
      </div>

    <div
      :class="`modal ${
        !open && 'opacity-0 pointer-events-none'
      } z-50 fixed w-full h-full top-0 left-0 flex items-center justify-center`">
      <div
        @click="open = false"
        class="absolute w-full h-full bg-gray-900 opacity-50 modal-overlay">
      </div>

      <div class="z-50 w-11/12 mx-auto overflow-y-auto bg-white rounded shadow-lg modal-container md:max-w-md">
        <div class="px-6 py-4 text-left modal-content">
          <!--Title-->
          <div class="flex items-center justify-between pb-3">
            <p class="text-2xl font-bold mt-2">스케줄 등록하기</p>
            <div class="z-50 cursor-pointer modal-close" @click="open = false">
              <svg
                class="text-black fill-current"
                xmlns="http://www.w3.org/2000/svg"
                width="18"
                height="18"
                viewBox="0 0 18 18">
                <path d="M14.53 4.53l-1.06-1.06L9 7.94 4.53 3.47 3.47 4.53 7.94 9l-4.47 4.47 1.06 1.06L9 10.06l4.47 4.47 1.06-1.06L10.06 9z" />
              </svg>
            </div>
          </div>

          <!--Body-->
          <div>
              <div class="mt-3">
                <label class="text-gray-700 ml-2">
                    스케줄 이름
                </label>
                <input
                  class="block w-full p-2 my-1 border border-gray-200 rounded hover:border-gray-400 focus:outline-none focus:border-gray-400"
                  type="text"
                  v-model="job.jobName"/>
              </div>

              <div class="mt-3">
                <label class="text-gray-700 ml-2">
                    스케줄 그룹
                </label>
                <input
                  class="block w-full p-2 my-1 border border-gray-200 rounded hover:border-gray-400 focus:outline-none focus:border-gray-400"
                  type="text"
                  v-model="job.jobGroup"/>
              </div>

              <div class="mt-3">
                <label class="text-gray-700 ml-2">
                    클래스 이름
                </label>
                <input
                  class="block w-full p-2 my-1 border border-gray-200 rounded hover:border-gray-400 focus:outline-none focus:border-gray-400"
                  type="text"
                  v-model="job.jobClass"/>
              </div>

              <div class="mt-3">
                <label class="text-gray-700 ml-2">
                    설명 (선택)
                </label>
                <input
                  class="block w-full p-2 my-1 border border-gray-200 rounded hover:border-gray-400 focus:outline-none focus:border-gray-400"
                  type="text"
                  placeholder="메모할 사항이 있으시면 입력해주세요."
                  v-model="job.jobDesc"/>
              </div>

              <div class="mt-3">
                <label class="text-gray-700 ml-2">
                    시작 날짜
                </label>
                <Datepicker v-model="date.startTime"></Datepicker>
              </div>

              <div class="mt-3">
                <label class="text-gray-700 ml-2">
                    종료 날짜
                </label>
                <Datepicker v-model="date.endTime"></Datepicker>
              </div>

              <div class="mt-3">
                <label class="text-gray-700 ml-2">
                    트리거 타입
                </label>
                <div class="block w-full p-2">
                  <input
                    class="w-5 h-5 form-check-input appearance-none rounded-full border border-gray-200 bg-white hover:bg-indigo-300 checked:bg-indigo-500 checked:border-indigo-600 align-middle cursor-pointer"
                    type="radio"
                    value="CRON"
                    v-model="job.trigger"/>
                  <span class="ml-2 text-gray-700">Cron Job</span>
                  <input
                    class="w-5 h-5 ml-5 form-check-input appearance-none rounded-full border border-gray-200 bg-white hover:bg-indigo-300 checked:bg-indigo-500 checked:border-indigo-600 align-middle cursor-pointer"
                    type="radio"
                    value="SIMPLE"
                    v-model="job.trigger"/>
                  <span class="ml-2 text-gray-700">Simple Job</span>
                </div>
              </div>

              <div v-show="job.trigger == 'CRON'">
                <div>
                  <label class="text-gray-700 ml-2">
                      크론 표현식
                  </label>
                  <input
                    class="block w-full p-2 my-1 border border-gray-200 rounded hover:border-gray-400 focus:outline-none focus:border-gray-400"
                    type="text"
                    @keyup.enter="itemsCheck()"
                    v-model="job.cronExpression"/>
                </div>
              </div>
              <div v-show="job.trigger == 'SIMPLE'">
                <div>
                  <label class="text-gray-700 ml-2">
                      반복 간격
                  </label>
                  <input
                    class="block w-full p-2 my-1 border border-gray-200 rounded hover:border-gray-400 focus:outline-none focus:border-gray-400"
                    type="text"
                    placeholder="초 단위로 입력해주세요."
                    v-model="job.repeatInterval"/>
                </div>
                
                <div class="mt-3">
                  <label class="text-gray-700 ml-2">
                      반복 횟수
                  </label>
                  <input
                    class="block w-full p-2 my-1 border border-gray-200 rounded hover:border-gray-400 focus:outline-none focus:border-gray-400"
                    type="text"
                    @keyup.enter="itemsCheck()"
                    v-model="job.repeatCount"/>
                </div>
              </div>
            </div>

          <!--Footer-->
          <div class="flex justify-end pt-7">
            <button
              @click="itemsCheck()"
              class="px-6 py-3 font-medium tracking-wide text-white bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none">
              등록
            </button>
            <button
              @click="cancelAdd()"
              class="px-6 py-3 mx-2 text-indigo-500 bg-transparent rounded-lg hover:bg-gray-100 hover:text-indigo-400 focus:outline-none">
              취소
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SchedulerApi from '@/api/SchedulerApi.js'


export default {
  name: 'ScheduleAdd',
  data () {
    return {
      open: false,

      job: {
        //공통
        jobName : '',
        jobGroup : '',
        jobClass: '',
        startDt: '',
        endDt: '',
        jobDesc: '',
        trigger: '',
        jobDataKey: 'sampleKey',
        jobDataValue: 'sampleValue',

        //cron job
        cronExpression: '',

        //simple job
        repeatInterval: '',
        repeatCount: '',
      },

      date: {
        startTime: '',
        endTime: '',
      }
      
    }
  },

  computed: {
  },

  methods: {
    itemsCheck(){
      if(this.isNull()){
        if(this.job.trigger == 'SIMPLE'){
          if(this.job.repeatInterval <= 0 || this.job.repeatCount <= 0){
          alert('반복 간격 및 횟수는 0보다 큰 정수를 입력해주세요!');
          }else{
            this.addJob();
          }
        }else{
          this.addJob();
        }
      }else{
        alert(`필수 입력 항목을 다 작성했는지 확인해주시고, 다시 시도해주세요!`);
      }
    },

    async addJob(){
      try {
        this.job.startDt = this.setDt(this.date.startTime);
        this.job.endDt = this.setDt(this.date.endTime);
        const data = await SchedulerApi.scheduleCreate(this.job);
        if(data.success == true){
          alert("스케줄 등록이 완료되었습니다!");
          this.$router.go();
        }
      } catch (error) {
        console.log(error);
      }
    },

    cancelAdd(){
      this.job = {};
      this.open = false;
    },

    setDt(time){
      return new Date(+time + 3240*10000).toISOString().replace("T", " ").replace(/\..*/, '');
    },

    isNull(){
      if(this.job.jobName != '' && this.job.jobGroup != '' && this.job.jobClass != '' && 
       this.date.startTime != '' && this.date.endTime != '' && this.job.trigger != ''){
        if(this.job.trigger == 'CRON'){
          if(this.job.cronExpression == ''){
            return false;
          }
        }else if(this.job.trigger == 'SIMPLE'){
          if(this.job.repeatInterval == '' || this.job.repeatCount == ''){
            return false;
          }
        }
      }else{
          return false;
      }
      return true;
    },

  }
}
</script>

<style>
.modal {
  transition: opacity 0.25s ease;
}
</style>