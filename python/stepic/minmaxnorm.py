nums = tuple(map(int, (input(), input(), input())))

if len(set(nums)) <= 2:
    other = (lambda x: nums[0] if (nums[0] == nums[1] or nums[0] == nums[2]) else nums[1])(nums[0])
else:    
    other = list(filter((lambda x: min(nums) < x < max(nums)), nums))[0]

print(max(nums))
print(min(nums))
print(other)

