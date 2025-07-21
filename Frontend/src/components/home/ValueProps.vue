<script setup>
defineProps({
    icon: String
})
</script>
 
<template>
    <div class="bg-[#F8F6F5] w-23 h-9 md:w-46 lg:w-79 md:h-15 lg:h-25 font-rubik text-[1rem] px-3 md:px-5.5 font-medium text-[#332A1E] flex justify-center items-center gap-[1vw] rounded-xl lg:rounded-xl shadow-[0_0.17rem_0.17rem_0_rgba(0,0,0,0.25)]">
        <img :src="icon" alt="Icon" class=" w-3 md:w-7 md:h-5 lg:w-8 lg:h-7">
        <p class=" text-[5px] md:text-[8px] lg:text-[15px]"><slot name="text">Text</slot></p>
    </div>
</template>
 
<style scoped>

</style>