<script setup>
defineProps({
  phones: {
    type: Array,
    required: true,
  },
  selectedIndex: {
    type: Number,
    required: true,
  },
})

const emit = defineEmits(['update:selectedIndex']);

const updateSelected = (index) => {
  emit('update:selectedIndex', index);
}
</script>
 
<template>
<div>
    <div class="flex mt-[1.5vw] ml-[-1.5vw] items-center">
        <button
            @click="updateSelected(selectedIndex === 0 ? phones.length - 1 : selectedIndex - 1)"
            class="text-[#6F879C] text-[3vw] px-[1.5vw] rounded-full h-fit transition duration-200 hover:bg-[#6F879C]/10"
        >
        <
        </button>
        <div class="flex w-[36vw]">
            <div
                v-for="(phone, index) in phones"
                :key="index"
                class="bg-[#F0EDEC] w-[8vw] h-[9vw] p-[1.6vw] m-[0.5vw] rounded-xl flex items-center justify-center"
                :class="{ 'border-[0.25vw] border-[#6F879C]': selectedIndex === index }"
                @click="updateSelected(index)"
            >
                <img :src="phone.thumbnail" :alt="`Phone ${index}`" class="h-[6vw] w-[7vw] object-cover scale-125"/>
            </div>
        </div>
        <button
            @click="updateSelected(selectedIndex === phones.length - 1 ? 0 : selectedIndex + 1)"
            class="text-[#6F879C] text-[3vw] px-[1.5vw] rounded-full h-fit transition duration-200 hover:bg-[#6F879C]/10"
        >
        >
        </button>
    </div>
</div>
</template>
 
<style scoped>

</style>