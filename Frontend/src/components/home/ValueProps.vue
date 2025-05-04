<script setup>
defineProps({
    icon: String
})
</script>
 
<template>
    <div class="bg-[#F8F6F5] w-[22vw] h-[7vw] font-rubik text-[1.2vw] px-[1.7vw] font-medium text-[#332A1E] flex justify-center items-center gap-[1vw] rounded-[1vw] shadow-[0_0.2vw_0.2vw_0_rgba(0,0,0,0.25)]">
        <img :src="icon" alt="Icon" class="w-[2vw]">
        <p><slot name="text">Text</slot></p>
    </div>
</template>
 
<style scoped>

</style>