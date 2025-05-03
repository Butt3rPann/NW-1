<script setup>
defineProps({
  image: {
    type: String,
    required: true,
  },
})
</script>
 
<template>
<div>
    <div>
        <div class="bg-[#F0EDEC] w-[41vw] h-[46vw] rounded-2xl flex items-center justify-center">
            <img :src="image" alt="Selected Phone" class="w-[29vw]">
        </div>
    </div>
</div>
</template>
 
<style scoped>

</style>