<script setup>
import nenePic from '@/assets/images/nenePic.jpg'
import noeyPic from '@/assets/images/noeyPic.jpg'
import proudPic from '@/assets/images/proudPic.jpg'
import ContactCard from '@/components/elements/ContactCard.vue'

const members = [
    {
        name: 'Chanya Nimtavorn',
        position: 'Developer',
        githubLink: 'https://github.com/CNProud',
        igLink: 'https://www.instagram.com/pr_channi/',
        img: proudPic
    },
    {
        name: 'Nontharat Thanapipatchai',
        position: 'Developer',
        githubLink: 'https://github.com/Naynay00066',
        igLink: 'https://www.instagram.com/nnaynayxo/',
        img: nenePic
    },
    {
        name: 'Pannaporn Anothaisinthavee',
        position: 'Developer',
        githubLink: 'https://github.com/Butt3rPann',
        igLink: 'https://www.instagram.com/butt3r_pann/',
        img: noeyPic
    }
]
</script>
 
<template>
<div class="w-full min-h-screen font-rubik bg-white p-20 pt-0 pl-7 pr-7 pb-10 sm:p-25 sm:pt-0 sm:pl-12 sm:pr-12 lg:p-35 lg:pl-20 lg:pr-20 lg:pt-0 text-[#332A1E]">
    <div class="w-full h-50 md:h-80 lg:h-90 flex justify-center items-center bg-[#ABBCC9]">
       <p class="font-bold text-xl md:text-3xl lg:text-5xl text-white text-shadow-gray-500 text-shadow-2xs">Our Team Members</p> 
    </div>
    <div class="flex flex-col sm:flex-row items-center justify-center gap-x-10 -mt-25 md:-mt-30 lg:-mt-35">
       <ContactCard :members="members" /> 
    </div>
    
</div>
</template>
 
<style scoped>

</style>