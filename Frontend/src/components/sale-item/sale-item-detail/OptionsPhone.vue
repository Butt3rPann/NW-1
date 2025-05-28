<script setup>
const props = defineProps({
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
    <div class="flex mt-4 items-center">
      <button @click="updateSelected(selectedIndex === 0 ? phones.length - 1 : selectedIndex - 1)"
        class="text-[#6F879C] text-4xl w-12 h-14 rounded-full flex items-center justify-center transition duration-200 hover:bg-[#6F879C]/10">
      <
      </button>
      <div class="flex w-100 justify-around">
        <div v-for="(thumbnail, index) in phones" :key="index"
          class="bg-[#F0EDEC] w-27 h-25 p-4 m-1.5 rounded-xl flex items-center justify-center overflow-hidden"
          :class="{ 'border-[0.25vw] border-[#6F879C]': selectedIndex === index }" @click="updateSelected(index)">
          <img :src="thumbnail" :alt="`Phone ${index}`" class="h-12 object-cover scale-125" />
        </div>
      </div>
      <button @click="updateSelected(selectedIndex === phones.length - 1 ? 0 : selectedIndex + 1)"
        class="text-[#6F879C] text-4xl w-12 h-14 rounded-full flex items-center justify-center transition duration-200 hover:bg-[#6F879C]/10">
      >
      </button>
    </div>
  </div>
</template>

<style scoped></style>
