<script setup>
import githubIcon from '@/assets/images/github.png'
import igIcon from '@/assets/images/instagram.png'
const props = defineProps({
    members: {
        type: Array,
        required: true
    }
})
</script>
 
<template>
<div v-for="member in members" class="overflow-hidden rounded-2xl bg-white p-6 shadow-lg hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-2 mt-10 w-full md:h-[370px] lg:h-[450px] md:w-72 lg:w-80">
    <div class="flex flex-col items-center justify-between h-full">   
        <div class="flex flex-col items-center">
            <img :src="member.img" alt="memberPic" class="w-32 h-32 md:w-30 md:h-30 lg:w-48 lg:h-48 rounded-full object-cover border-4 border-white shadow-xl">
            <p class="text-xl text-center md:text-lg lg:text-2xl font-bold mt-6 text-gray-800">
                {{ member.name }}
            </p>
            <p class="text-gray-500 text-base lg:text-lg mt-2 font-medium">{{ member.position }}</p>            
        </div>   
        <div class="flex items-center justify-end gap-x-4 mt-4 sm:mt-0">
            <a :href="member.githubLink" class="p-3 rounded-full bg-gray-100 hover:shadow-lg">
                <img :src="githubIcon" alt="githubIcon" class="w-5 h-5 md:w-6 md:h-6 lg:w-7 lg:h-7">
            </a>
            <a :href="member.igLink" class="p-3 rounded-full bg-gray-100 hover:shadow-lg">
                <img :src="igIcon" alt="igIcon" class="w-5 h-5 md:w-6 md:h-6 lg:w-7 lg:h-7">
            </a>
        </div>
    </div>
</div>
</template>
 
<style scoped>

</style>