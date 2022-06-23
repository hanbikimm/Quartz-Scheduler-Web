<template>
    
    <tbody class="bg-white">
        <tr  
        class="hover:bg-gray-50 focus:outline-none cursor-pointer">
            <td class="px-6 py-4 text-sm leading-5 text-gray-500 border-b border-gray-200 whitespace-nowrap">
                {{ job.name }} ({{ job.username }})
            </td>

            <td class="px-6 py-4 text-sm leading-5 text-gray-500 border-b border-gray-200 whitespace-nowrap">
                {{ job.jobName }}
            </td>

            <td class="px-6 py-4 text-sm leading-5 text-gray-500 border-b border-gray-200 whitespace-nowrap">
                {{ job.jobGroup}}
            </td>

            <td class="px-6 py-4 text-sm leading-5 text-gray-500 border-b border-gray-200 whitespace-nowrap">
                {{ job.prevFireDt }}
            </td>

            <td class="px-6 py-4 text-sm leading-5 text-gray-500 border-b border-gray-200 whitespace-nowrap">
                {{ job.nextFireDt }}
            </td>

            <td class="px-6 py-4 border-b border-gray-200 whitespace-nowrap">
                <span v-if="job.jobState == 'SCHEDULED'"
                class="inline-flex px-2 text-xs font-semibold leading-5 text-indigo-800 bg-indigo-100 rounded-full">
                실행 대기
                </span>

                <span v-else-if="job.jobState == 'RUNNING'"
                class="inline-flex px-2 text-xs font-semibold leading-5 text-green-800 bg-green-100 rounded-full">
                실행 중
                </span>

                <span v-else-if="job.jobState == 'PAUSED'"
                class="inline-flex px-2 text-xs font-semibold leading-5 text-pink-800 bg-pink-100 rounded-full">
                실행 정지
                </span>

                <span v-else-if="job.jobState == 'UNKNOWN'"
                class="inline-flex px-2 text-xs font-semibold leading-5 text-yellow-800 bg-yellow-100 rounded-full">
                실행 오류
                </span>
            </td>
        </tr>
    </tbody>
</template>

<script>
export default {
    name: 'ScheduleListItem',
    props:{
        job:{
            type: Object,
            required: true,
        },
    },

}
</script>

<style>

</style>