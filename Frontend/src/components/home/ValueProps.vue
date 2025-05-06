<script setup>
defineProps({
    icon: String
})
</script>
 
<template>
    <div class="bg-[#F8F6F5] w-79 h-25 font-rubik text-[1rem] px-6.5 font-medium text-[#332A1E] flex justify-center items-center gap-[1vw] rounded-xl shadow-[0_0.17rem_0.17rem_0_rgba(0,0,0,0.25)]">
        <img :src="icon" alt="Icon" class="w-7">
        <p><slot name="text">Text</slot></p>
    </div>
</template>
 
<style scoped>

</style>