#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Jun  7 19:28:11 2017

@author: nikolay
"""

h = """<table class="wikitable sortable jquery-tablesorter">
<thead><tr>
<th scope="col" class="headerSort" tabindex="0" role="columnheader button" title="Sort ascending">Mnemonic</th>
<th scope="col" class="headerSort" tabindex="0" role="columnheader button" title="Sort ascending">Opcode<br>
<i>(in hexadecimal)</i></th>
<th class="headerSort" tabindex="0" role="columnheader button" title="Sort ascending">Opcode (in binary)</th>
<th scope="col" class="headerSort" tabindex="0" role="columnheader button" title="Sort ascending">Other bytes</th>
<th scope="col" class="headerSort" tabindex="0" role="columnheader button" title="Sort ascending">Stack<br>
[before]→[after]</th>
<th scope="col" class="unsortable">Description</th>
</tr></thead><tbody>
<tr>
<td>aaload</td>
<td>32</td>
<td>0011 0010</td>
<td></td>
<td>arrayref, index → value</td>
<td>load onto the stack a reference from an array</td>
</tr>
<tr>
<td>aastore</td>
<td>53</td>
<td>0101 0011</td>
<td></td>
<td>arrayref, index, value →</td>
<td>store into a reference in an array</td>
</tr>
<tr>
<td>aconst_null</td>
<td>01</td>
<td>0000 0001</td>
<td></td>
<td>→ null</td>
<td>push a <i>null</i> reference onto the stack</td>
</tr>
<tr>
<td>aload</td>
<td>19</td>
<td>0001 1001</td>
<td>1: index</td>
<td>→ objectref</td>
<td>load a reference onto the stack from a local variable <i>#index</i></td>
</tr>
<tr>
<td>aload_0</td>
<td>2a</td>
<td>0010 1010</td>
<td></td>
<td>→ objectref</td>
<td>load a reference onto the stack from local variable 0</td>
</tr>
<tr>
<td>aload_1</td>
<td>2b</td>
<td>0010 1011</td>
<td></td>
<td>→ objectref</td>
<td>load a reference onto the stack from local variable 1</td>
</tr>
<tr>
<td>aload_2</td>
<td>2c</td>
<td>0010 1100</td>
<td></td>
<td>→ objectref</td>
<td>load a reference onto the stack from local variable 2</td>
</tr>
<tr>
<td>aload_3</td>
<td>2d</td>
<td>0010 1101</td>
<td></td>
<td>→ objectref</td>
<td>load a reference onto the stack from local variable 3</td>
</tr>
<tr>
<td>anewarray</td>
<td>bd</td>
<td>1011 1101</td>
<td>2: indexbyte1, indexbyte2</td>
<td>count → arrayref</td>
<td>create a new array of references of length <i>count</i> and component type identified by the class reference <i>index</i> (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>) in the constant pool</td>
</tr>
<tr>
<td>areturn</td>
<td>b0</td>
<td>1011 0000</td>
<td></td>
<td>objectref → [empty]</td>
<td>return a reference from a method</td>
</tr>
<tr>
<td>arraylength</td>
<td>be</td>
<td>1011 1110</td>
<td></td>
<td>arrayref → length</td>
<td>get the length of an array</td>
</tr>
<tr>
<td>astore</td>
<td>3a</td>
<td>0011 1010</td>
<td>1: index</td>
<td>objectref →</td>
<td>store a reference into a local variable <i>#index</i></td>
</tr>
<tr>
<td>astore_0</td>
<td>4b</td>
<td>0100 1011</td>
<td></td>
<td>objectref →</td>
<td>store a reference into local variable 0</td>
</tr>
<tr>
<td>astore_1</td>
<td>4c</td>
<td>0100 1100</td>
<td></td>
<td>objectref →</td>
<td>store a reference into local variable 1</td>
</tr>
<tr>
<td>astore_2</td>
<td>4d</td>
<td>0100 1101</td>
<td></td>
<td>objectref →</td>
<td>store a reference into local variable 2</td>
</tr>
<tr>
<td>astore_3</td>
<td>4e</td>
<td>0100 1110</td>
<td></td>
<td>objectref →</td>
<td>store a reference into local variable 3</td>
</tr>
<tr>
<td>athrow</td>
<td>bf</td>
<td>1011 1111</td>
<td></td>
<td>objectref → [empty], objectref</td>
<td>throws an error or exception (notice that the rest of the stack is cleared, leaving only a reference to the Throwable)</td>
</tr>
<tr>
<td>baload</td>
<td>33</td>
<td>0011 0011</td>
<td></td>
<td>arrayref, index → value</td>
<td>load a byte or Boolean value from an array</td>
</tr>
<tr>
<td>bastore</td>
<td>54</td>
<td>0101 0100</td>
<td></td>
<td>arrayref, index, value →</td>
<td>store a byte or Boolean value into an array</td>
</tr>
<tr>
<td>bipush</td>
<td>10</td>
<td>0001 0000</td>
<td>1: byte</td>
<td>→ value</td>
<td>push a <i>byte</i> onto the stack as an integer <i>value</i></td>
</tr>
<tr>
<td>breakpoint</td>
<td>ca</td>
<td>1100 1010</td>
<td></td>
<td></td>
<td>reserved for breakpoints in Java debuggers; should not appear in any class file</td>
</tr>
<tr>
<td>caload</td>
<td>34</td>
<td>0011 0100</td>
<td></td>
<td>arrayref, index → value</td>
<td>load a char from an array</td>
</tr>
<tr>
<td>castore</td>
<td>55</td>
<td>0101 0101</td>
<td></td>
<td>arrayref, index, value →</td>
<td>store a char into an array</td>
</tr>
<tr>
<td>checkcast</td>
<td>c0</td>
<td>1100 0000</td>
<td>2: indexbyte1, indexbyte2</td>
<td>objectref → objectref</td>
<td>checks whether an <i>objectref</i> is of a certain type, the class reference of which is in the constant pool at <i>index</i> (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>d2f</td>
<td>90</td>
<td>1001 0000</td>
<td></td>
<td>value → result</td>
<td>convert a double to a float</td>
</tr>
<tr>
<td>d2i</td>
<td>8e</td>
<td>1000 1110</td>
<td></td>
<td>value → result</td>
<td>convert a double to an int</td>
</tr>
<tr>
<td>d2l</td>
<td>8f</td>
<td>1000 1111</td>
<td></td>
<td>value → result</td>
<td>convert a double to a long</td>
</tr>
<tr>
<td>dadd</td>
<td>63</td>
<td>0110 0011</td>
<td></td>
<td>value1, value2 → result</td>
<td>add two doubles</td>
</tr>
<tr>
<td>daload</td>
<td>31</td>
<td>0011 0001</td>
<td></td>
<td>arrayref, index → value</td>
<td>load a double from an array</td>
</tr>
<tr>
<td>dastore</td>
<td>52</td>
<td>0101 0010</td>
<td></td>
<td>arrayref, index, value →</td>
<td>store a double into an array</td>
</tr>
<tr>
<td>dcmpg</td>
<td>98</td>
<td>1001 1000</td>
<td></td>
<td>value1, value2 → result</td>
<td>compare two doubles</td>
</tr>
<tr>
<td>dcmpl</td>
<td>97</td>
<td>1001 0111</td>
<td></td>
<td>value1, value2 → result</td>
<td>compare two doubles</td>
</tr>
<tr>
<td>dconst_0</td>
<td>0e</td>
<td>0000 1110</td>
<td></td>
<td>→ 0.0</td>
<td>push the constant <i>0.0</i> (a <i>double</i>) onto the stack</td>
</tr>
<tr>
<td>dconst_1</td>
<td>0f</td>
<td>0000 1111</td>
<td></td>
<td>→ 1.0</td>
<td>push the constant <i>1.0</i> (a <i>double</i>) onto the stack</td>
</tr>
<tr>
<td>ddiv</td>
<td>6f</td>
<td>0110 1111</td>
<td></td>
<td>value1, value2 → result</td>
<td>divide two doubles</td>
</tr>
<tr>
<td>dload</td>
<td>18</td>
<td>0001 1000</td>
<td>1: index</td>
<td>→ value</td>
<td>load a double <i>value</i> from a local variable <i>#index</i></td>
</tr>
<tr>
<td>dload_0</td>
<td>26</td>
<td>0010 0110</td>
<td></td>
<td>→ value</td>
<td>load a double from local variable 0</td>
</tr>
<tr>
<td>dload_1</td>
<td>27</td>
<td>0010 0111</td>
<td></td>
<td>→ value</td>
<td>load a double from local variable 1</td>
</tr>
<tr>
<td>dload_2</td>
<td>28</td>
<td>0010 1000</td>
<td></td>
<td>→ value</td>
<td>load a double from local variable 2</td>
</tr>
<tr>
<td>dload_3</td>
<td>29</td>
<td>0010 1001</td>
<td></td>
<td>→ value</td>
<td>load a double from local variable 3</td>
</tr>
<tr>
<td>dmul</td>
<td>6b</td>
<td>0110 1011</td>
<td></td>
<td>value1, value2 → result</td>
<td>multiply two doubles</td>
</tr>
<tr>
<td>dneg</td>
<td>77</td>
<td>0111 0111</td>
<td></td>
<td>value → result</td>
<td>negate a double</td>
</tr>
<tr>
<td>drem</td>
<td>73</td>
<td>0111 0011</td>
<td></td>
<td>value1, value2 → result</td>
<td>get the remainder from a division between two doubles</td>
</tr>
<tr>
<td>dreturn</td>
<td>af</td>
<td>1010 1111</td>
<td></td>
<td>value → [empty]</td>
<td>return a double from a method</td>
</tr>
<tr>
<td>dstore</td>
<td>39</td>
<td>0011 1001</td>
<td>1: index</td>
<td>value →</td>
<td>store a double <i>value</i> into a local variable <i>#index</i></td>
</tr>
<tr>
<td>dstore_0</td>
<td>47</td>
<td>0100 0111</td>
<td></td>
<td>value →</td>
<td>store a double into local variable 0</td>
</tr>
<tr>
<td>dstore_1</td>
<td>48</td>
<td>0100 1000</td>
<td></td>
<td>value →</td>
<td>store a double into local variable 1</td>
</tr>
<tr>
<td>dstore_2</td>
<td>49</td>
<td>0100 1001</td>
<td></td>
<td>value →</td>
<td>store a double into local variable 2</td>
</tr>
<tr>
<td>dstore_3</td>
<td>4a</td>
<td>0100 1010</td>
<td></td>
<td>value →</td>
<td>store a double into local variable 3</td>
</tr>
<tr>
<td>dsub</td>
<td>67</td>
<td>0110 0111</td>
<td></td>
<td>value1, value2 → result</td>
<td>subtract a double from another</td>
</tr>
<tr>
<td>dup</td>
<td>59</td>
<td>0101 1001</td>
<td></td>
<td>value → value, value</td>
<td>duplicate the value on top of the stack</td>
</tr>
<tr>
<td>dup_x1</td>
<td>5a</td>
<td>0101 1010</td>
<td></td>
<td>value2, value1 → value1, value2, value1</td>
<td>insert a copy of the top value into the stack two values from the top. value1 and value2 must not be of the type double or long.</td>
</tr>
<tr>
<td>dup_x2</td>
<td>5b</td>
<td>0101 1011</td>
<td></td>
<td>value3, value2, value1 → value1, value3, value2, value1</td>
<td>insert a copy of the top value into the stack two (if value2 is double or long it takes up the entry of value3, too) or three values (if value2 is neither double nor long) from the top</td>
</tr>
<tr>
<td>dup2</td>
<td>5c</td>
<td>0101 1100</td>
<td></td>
<td>{value2, value1} → {value2, value1}, {value2, value1}</td>
<td>duplicate top two stack words (two values, if value1 is not double nor long; a single value, if value1 is double or long)</td>
</tr>
<tr>
<td>dup2_x1</td>
<td>5d</td>
<td>0101 1101</td>
<td></td>
<td>value3, {value2, value1} → {value2, value1}, value3, {value2, value1}</td>
<td>duplicate two words and insert beneath third word (see explanation above)</td>
</tr>
<tr>
<td>dup2_x2</td>
<td>5e</td>
<td>0101 1110</td>
<td></td>
<td>{value4, value3}, {value2, value1} → {value2, value1}, {value4, value3}, {value2, value1}</td>
<td>duplicate two words and insert beneath fourth word</td>
</tr>
<tr>
<td>f2d</td>
<td>8d</td>
<td>1000 1101</td>
<td></td>
<td>value → result</td>
<td>convert a float to a double</td>
</tr>
<tr>
<td>f2i</td>
<td>8b</td>
<td>1000 1011</td>
<td></td>
<td>value → result</td>
<td>convert a float to an int</td>
</tr>
<tr>
<td>f2l</td>
<td>8c</td>
<td>1000 1100</td>
<td></td>
<td>value → result</td>
<td>convert a float to a long</td>
</tr>
<tr>
<td>fadd</td>
<td>62</td>
<td>0110 0010</td>
<td></td>
<td>value1, value2 → result</td>
<td>add two floats</td>
</tr>
<tr>
<td>faload</td>
<td>30</td>
<td>0011 0000</td>
<td></td>
<td>arrayref, index → value</td>
<td>load a float from an array</td>
</tr>
<tr>
<td>fastore</td>
<td>51</td>
<td>0101 0001</td>
<td></td>
<td>arrayref, index, value →</td>
<td>store a float in an array</td>
</tr>
<tr>
<td>fcmpg</td>
<td>96</td>
<td>1001 0110</td>
<td></td>
<td>value1, value2 → result</td>
<td>compare two floats</td>
</tr>
<tr>
<td>fcmpl</td>
<td>95</td>
<td>1001 0101</td>
<td></td>
<td>value1, value2 → result</td>
<td>compare two floats</td>
</tr>
<tr>
<td>fconst_0</td>
<td>0b</td>
<td>0000 1011</td>
<td></td>
<td>→ 0.0f</td>
<td>push <i>0.0f</i> on the stack</td>
</tr>
<tr>
<td>fconst_1</td>
<td>0c</td>
<td>0000 1100</td>
<td></td>
<td>→ 1.0f</td>
<td>push <i>1.0f</i> on the stack</td>
</tr>
<tr>
<td>fconst_2</td>
<td>0d</td>
<td>0000 1101</td>
<td></td>
<td>→ 2.0f</td>
<td>push <i>2.0f</i> on the stack</td>
</tr>
<tr>
<td>fdiv</td>
<td>6e</td>
<td>0110 1110</td>
<td></td>
<td>value1, value2 → result</td>
<td>divide two floats</td>
</tr>
<tr>
<td>fload</td>
<td>17</td>
<td>0001 0111</td>
<td>1: index</td>
<td>→ value</td>
<td>load a float <i>value</i> from a local variable <i>#index</i></td>
</tr>
<tr>
<td>fload_0</td>
<td>22</td>
<td>0010 0010</td>
<td></td>
<td>→ value</td>
<td>load a float <i>value</i> from local variable 0</td>
</tr>
<tr>
<td>fload_1</td>
<td>23</td>
<td>0010 0011</td>
<td></td>
<td>→ value</td>
<td>load a float <i>value</i> from local variable 1</td>
</tr>
<tr>
<td>fload_2</td>
<td>24</td>
<td>0010 0100</td>
<td></td>
<td>→ value</td>
<td>load a float <i>value</i> from local variable 2</td>
</tr>
<tr>
<td>fload_3</td>
<td>25</td>
<td>0010 0101</td>
<td></td>
<td>→ value</td>
<td>load a float <i>value</i> from local variable 3</td>
</tr>
<tr>
<td>fmul</td>
<td>6a</td>
<td>0110 1010</td>
<td></td>
<td>value1, value2 → result</td>
<td>multiply two floats</td>
</tr>
<tr>
<td>fneg</td>
<td>76</td>
<td>0111 0110</td>
<td></td>
<td>value → result</td>
<td>negate a float</td>
</tr>
<tr>
<td>frem</td>
<td>72</td>
<td>0111 0010</td>
<td></td>
<td>value1, value2 → result</td>
<td>get the remainder from a division between two floats</td>
</tr>
<tr>
<td>freturn</td>
<td>ae</td>
<td>1010 1110</td>
<td></td>
<td>value → [empty]</td>
<td>return a float</td>
</tr>
<tr>
<td>fstore</td>
<td>38</td>
<td>0011 1000</td>
<td>1: index</td>
<td>value →</td>
<td>store a float <i>value</i> into a local variable <i>#index</i></td>
</tr>
<tr>
<td>fstore_0</td>
<td>43</td>
<td>0100 0011</td>
<td></td>
<td>value →</td>
<td>store a float <i>value</i> into local variable 0</td>
</tr>
<tr>
<td>fstore_1</td>
<td>44</td>
<td>0100 0100</td>
<td></td>
<td>value →</td>
<td>store a float <i>value</i> into local variable 1</td>
</tr>
<tr>
<td>fstore_2</td>
<td>45</td>
<td>0100 0101</td>
<td></td>
<td>value →</td>
<td>store a float <i>value</i> into local variable 2</td>
</tr>
<tr>
<td>fstore_3</td>
<td>46</td>
<td>0100 0110</td>
<td></td>
<td>value →</td>
<td>store a float <i>value</i> into local variable 3</td>
</tr>
<tr>
<td>fsub</td>
<td>66</td>
<td>0110 0110</td>
<td></td>
<td>value1, value2 → result</td>
<td>subtract two floats</td>
</tr>
<tr>
<td>getfield</td>
<td>b4</td>
<td>1011 0100</td>
<td>2: indexbyte1, indexbyte2</td>
<td>objectref → value</td>
<td>get a field <i>value</i> of an object <i>objectref</i>, where the field is identified by field reference in the constant pool <i>index</i> (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>getstatic</td>
<td>b2</td>
<td>1011 0010</td>
<td>2: indexbyte1, indexbyte2</td>
<td>→ value</td>
<td>get a static field <i>value</i> of a class, where the field is identified by field reference in the constant pool <i>index</i> (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>goto</td>
<td>a7</td>
<td>1010 0111</td>
<td>2: branchbyte1, branchbyte2</td>
<td>[no change]</td>
<td>goes to another instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>goto_w</td>
<td>c8</td>
<td>1100 1000</td>
<td>4: branchbyte1, branchbyte2, branchbyte3, branchbyte4</td>
<td>[no change]</td>
<td>goes to another instruction at <i>branchoffset</i> (signed int constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 24 + branchbyte2 &lt;&lt; 16 + branchbyte3 &lt;&lt; 8 + branchbyte4</span>)</td>
</tr>
<tr>
<td>i2b</td>
<td>91</td>
<td>1001 0001</td>
<td></td>
<td>value → result</td>
<td>convert an int into a byte</td>
</tr>
<tr>
<td>i2c</td>
<td>92</td>
<td>1001 0010</td>
<td></td>
<td>value → result</td>
<td>convert an int into a character</td>
</tr>
<tr>
<td>i2d</td>
<td>87</td>
<td>1000 0111</td>
<td></td>
<td>value → result</td>
<td>convert an int into a double</td>
</tr>
<tr>
<td>i2f</td>
<td>86</td>
<td>1000 0110</td>
<td></td>
<td>value → result</td>
<td>convert an int into a float</td>
</tr>
<tr>
<td>i2l</td>
<td>85</td>
<td>1000 0101</td>
<td></td>
<td>value → result</td>
<td>convert an int into a long</td>
</tr>
<tr>
<td>i2s</td>
<td>93</td>
<td>1001 0011</td>
<td></td>
<td>value → result</td>
<td>convert an int into a short</td>
</tr>
<tr>
<td>iadd</td>
<td>60</td>
<td>0110 0000</td>
<td></td>
<td>value1, value2 → result</td>
<td>add two ints</td>
</tr>
<tr>
<td>iaload</td>
<td>2e</td>
<td>0010 1110</td>
<td></td>
<td>arrayref, index → value</td>
<td>load an int from an array</td>
</tr>
<tr>
<td>iand</td>
<td>7e</td>
<td>0111 1110</td>
<td></td>
<td>value1, value2 → result</td>
<td>perform a bitwise AND on two integers</td>
</tr>
<tr>
<td>iastore</td>
<td>4f</td>
<td>0100 1111</td>
<td></td>
<td>arrayref, index, value →</td>
<td>store an int into an array</td>
</tr>
<tr>
<td>iconst_m1</td>
<td>02</td>
<td>0000 0010</td>
<td></td>
<td>→ -1</td>
<td>load the int value −1 onto the stack</td>
</tr>
<tr>
<td>iconst_0</td>
<td>03</td>
<td>0000 0011</td>
<td></td>
<td>→ 0</td>
<td>load the int value 0 onto the stack</td>
</tr>
<tr>
<td>iconst_1</td>
<td>04</td>
<td>0000 0100</td>
<td></td>
<td>→ 1</td>
<td>load the int value 1 onto the stack</td>
</tr>
<tr>
<td>iconst_2</td>
<td>05</td>
<td>0000 0101</td>
<td></td>
<td>→ 2</td>
<td>load the int value 2 onto the stack</td>
</tr>
<tr>
<td>iconst_3</td>
<td>06</td>
<td>0000 0110</td>
<td></td>
<td>→ 3</td>
<td>load the int value 3 onto the stack</td>
</tr>
<tr>
<td>iconst_4</td>
<td>07</td>
<td>0000 0111</td>
<td></td>
<td>→ 4</td>
<td>load the int value 4 onto the stack</td>
</tr>
<tr>
<td>iconst_5</td>
<td>08</td>
<td>0000 1000</td>
<td></td>
<td>→ 5</td>
<td>load the int value 5 onto the stack</td>
</tr>
<tr>
<td>idiv</td>
<td>6c</td>
<td>0110 1100</td>
<td></td>
<td>value1, value2 → result</td>
<td>divide two integers</td>
</tr>
<tr>
<td>if_acmpeq</td>
<td>a5</td>
<td>1010 0101</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value1, value2 →</td>
<td>if references are equal, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>if_acmpne</td>
<td>a6</td>
<td>1010 0110</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value1, value2 →</td>
<td>if references are not equal, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>if_icmpeq</td>
<td>9f</td>
<td>1001 1111</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value1, value2 →</td>
<td>if ints are equal, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>if_icmpge</td>
<td>a2</td>
<td>1010 0010</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value1, value2 →</td>
<td>if <i>value1</i> is greater than or equal to <i>value2</i>, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>if_icmpgt</td>
<td>a3</td>
<td>1010 0011</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value1, value2 →</td>
<td>if <i>value1</i> is greater than <i>value2</i>, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>if_icmple</td>
<td>a4</td>
<td>1010 0100</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value1, value2 →</td>
<td>if <i>value1</i> is less than or equal to <i>value2</i>, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>if_icmplt</td>
<td>a1</td>
<td>1010 0001</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value1, value2 →</td>
<td>if <i>value1</i> is less than <i>value2</i>, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>if_icmpne</td>
<td>a0</td>
<td>1010 0000</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value1, value2 →</td>
<td>if ints are not equal, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>ifeq</td>
<td>99</td>
<td>1001 1001</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value →</td>
<td>if <i>value</i> is 0, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>ifge</td>
<td>9c</td>
<td>1001 1100</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value →</td>
<td>if <i>value</i> is greater than or equal to 0, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>ifgt</td>
<td>9d</td>
<td>1001 1101</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value →</td>
<td>if <i>value</i> is greater than 0, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>ifle</td>
<td>9e</td>
<td>1001 1110</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value →</td>
<td>if <i>value</i> is less than or equal to 0, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>iflt</td>
<td>9b</td>
<td>1001 1011</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value →</td>
<td>if <i>value</i> is less than 0, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>ifne</td>
<td>9a</td>
<td>1001 1010</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value →</td>
<td>if <i>value</i> is not 0, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>ifnonnull</td>
<td>c7</td>
<td>1100 0111</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value →</td>
<td>if <i>value</i> is not null, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>ifnull</td>
<td>c6</td>
<td>1100 0110</td>
<td>2: branchbyte1, branchbyte2</td>
<td>value →</td>
<td>if <i>value</i> is null, branch to instruction at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>)</td>
</tr>
<tr>
<td>iinc</td>
<td>84</td>
<td>1000 0100</td>
<td>2: index, const</td>
<td>[No change]</td>
<td>increment local variable <i>#index</i> by signed byte <i>const</i></td>
</tr>
<tr>
<td>iload</td>
<td>15</td>
<td>0001 0101</td>
<td>1: index</td>
<td>→ value</td>
<td>load an int <i>value</i> from a local variable <i>#index</i></td>
</tr>
<tr>
<td>iload_0</td>
<td>1a</td>
<td>0001 1010</td>
<td></td>
<td>→ value</td>
<td>load an int <i>value</i> from local variable 0</td>
</tr>
<tr>
<td>iload_1</td>
<td>1b</td>
<td>0001 1011</td>
<td></td>
<td>→ value</td>
<td>load an int <i>value</i> from local variable 1</td>
</tr>
<tr>
<td>iload_2</td>
<td>1c</td>
<td>0001 1100</td>
<td></td>
<td>→ value</td>
<td>load an int <i>value</i> from local variable 2</td>
</tr>
<tr>
<td>iload_3</td>
<td>1d</td>
<td>0001 1101</td>
<td></td>
<td>→ value</td>
<td>load an int <i>value</i> from local variable 3</td>
</tr>
<tr>
<td>impdep1</td>
<td>fe</td>
<td>1111 1110</td>
<td></td>
<td></td>
<td>reserved for implementation-dependent operations within debuggers; should not appear in any class file</td>
</tr>
<tr>
<td>impdep2</td>
<td>ff</td>
<td>1111 1111</td>
<td></td>
<td></td>
<td>reserved for implementation-dependent operations within debuggers; should not appear in any class file</td>
</tr>
<tr>
<td>imul</td>
<td>68</td>
<td>0110 1000</td>
<td></td>
<td>value1, value2 → result</td>
<td>multiply two integers</td>
</tr>
<tr>
<td>ineg</td>
<td>74</td>
<td>0111 0100</td>
<td></td>
<td>value → result</td>
<td>negate int</td>
</tr>
<tr>
<td>instanceof</td>
<td>c1</td>
<td>1100 0001</td>
<td>2: indexbyte1, indexbyte2</td>
<td>objectref → result</td>
<td>determines if an object <i>objectref</i> is of a given type, identified by class reference <i>index</i> in constant pool (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>invokedynamic</td>
<td>ba</td>
<td>1011 1010</td>
<td>4: indexbyte1, indexbyte2, 0, 0</td>
<td>[arg1, [arg2 ...]] → result</td>
<td>invokes a dynamic method and puts the result on the stack (might be void); the method is identified by method reference <i>index</i> in constant pool (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>invokeinterface</td>
<td>b9</td>
<td>1011 1001</td>
<td>4: indexbyte1, indexbyte2, count, 0</td>
<td>objectref, [arg1, arg2, ...] → result</td>
<td>invokes an interface method on object <i>objectref</i> and puts the result on the stack (might be void); the interface method is identified by method reference <i>index</i> in constant pool (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>invokespecial</td>
<td>b7</td>
<td>1011 0111</td>
<td>2: indexbyte1, indexbyte2</td>
<td>objectref, [arg1, arg2, ...] → result</td>
<td>invoke instance method on object <i>objectref</i> and puts the result on the stack (might be void); the method is identified by method reference <i>index</i> in constant pool (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>invokestatic</td>
<td>b8</td>
<td>1011 1000</td>
<td>2: indexbyte1, indexbyte2</td>
<td>[arg1, arg2, ...] → result</td>
<td>invoke a static method and puts the result on the stack (might be void); the method is identified by method reference <i>index</i> in constant pool (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>invokevirtual</td>
<td>b6</td>
<td>1011 0110</td>
<td>2: indexbyte1, indexbyte2</td>
<td>objectref, [arg1, arg2, ...] → result</td>
<td>invoke virtual method on object <i>objectref</i> and puts the result on the stack (might be void); the method is identified by method reference <i>index</i> in constant pool (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>ior</td>
<td>80</td>
<td>1000 0000</td>
<td></td>
<td>value1, value2 → result</td>
<td>bitwise int OR</td>
</tr>
<tr>
<td>irem</td>
<td>70</td>
<td>0111 0000</td>
<td></td>
<td>value1, value2 → result</td>
<td>logical int remainder</td>
</tr>
<tr>
<td>ireturn</td>
<td>ac</td>
<td>1010 1100</td>
<td></td>
<td>value → [empty]</td>
<td>return an integer from a method</td>
</tr>
<tr>
<td>ishl</td>
<td>78</td>
<td>0111 1000</td>
<td></td>
<td>value1, value2 → result</td>
<td>int shift left</td>
</tr>
<tr>
<td>ishr</td>
<td>7a</td>
<td>0111 1010</td>
<td></td>
<td>value1, value2 → result</td>
<td>int arithmetic shift right</td>
</tr>
<tr>
<td>istore</td>
<td>36</td>
<td>0011 0110</td>
<td>1: index</td>
<td>value →</td>
<td>store int <i>value</i> into variable <i>#index</i></td>
</tr>
<tr>
<td>istore_0</td>
<td>3b</td>
<td>0011 1011</td>
<td></td>
<td>value →</td>
<td>store int <i>value</i> into variable 0</td>
</tr>
<tr>
<td>istore_1</td>
<td>3c</td>
<td>0011 1100</td>
<td></td>
<td>value →</td>
<td>store int <i>value</i> into variable 1</td>
</tr>
<tr>
<td>istore_2</td>
<td>3d</td>
<td>0011 1101</td>
<td></td>
<td>value →</td>
<td>store int <i>value</i> into variable 2</td>
</tr>
<tr>
<td>istore_3</td>
<td>3e</td>
<td>0011 1110</td>
<td></td>
<td>value →</td>
<td>store int <i>value</i> into variable 3</td>
</tr>
<tr>
<td>isub</td>
<td>64</td>
<td>0110 0100</td>
<td></td>
<td>value1, value2 → result</td>
<td>int subtract</td>
</tr>
<tr>
<td>iushr</td>
<td>7c</td>
<td>0111 1100</td>
<td></td>
<td>value1, value2 → result</td>
<td>int logical shift right</td>
</tr>
<tr>
<td>ixor</td>
<td>82</td>
<td>1000 0010</td>
<td></td>
<td>value1, value2 → result</td>
<td>int xor</td>
</tr>
<tr>
<td>jsr</td>
<td>a8</td>
<td>1010 1000</td>
<td>2: branchbyte1, branchbyte2</td>
<td>→ address</td>
<td>jump to subroutine at <i>branchoffset</i> (signed short constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 8 + branchbyte2</span>) and place the return address on the stack</td>
</tr>
<tr>
<td>jsr_w</td>
<td>c9</td>
<td>1100 1001</td>
<td>4: branchbyte1, branchbyte2, branchbyte3, branchbyte4</td>
<td>→ address</td>
<td>jump to subroutine at <i>branchoffset</i> (signed int constructed from unsigned bytes <span style="font-family: monospace, monospace;">branchbyte1 &lt;&lt; 24 + branchbyte2 &lt;&lt; 16 + branchbyte3 &lt;&lt; 8 + branchbyte4</span>) and place the return address on the stack</td>
</tr>
<tr>
<td>l2d</td>
<td>8a</td>
<td>1000 1010</td>
<td></td>
<td>value → result</td>
<td>convert a long to a double</td>
</tr>
<tr>
<td>l2f</td>
<td>89</td>
<td>1000 1001</td>
<td></td>
<td>value → result</td>
<td>convert a long to a float</td>
</tr>
<tr>
<td>l2i</td>
<td>88</td>
<td>1000 1000</td>
<td></td>
<td>value → result</td>
<td>convert a long to a int</td>
</tr>
<tr>
<td>ladd</td>
<td>61</td>
<td>0110 0001</td>
<td></td>
<td>value1, value2 → result</td>
<td>add two longs</td>
</tr>
<tr>
<td>laload</td>
<td>2f</td>
<td>0010 1111</td>
<td></td>
<td>arrayref, index → value</td>
<td>load a long from an array</td>
</tr>
<tr>
<td>land</td>
<td>7f</td>
<td>0111 1111</td>
<td></td>
<td>value1, value2 → result</td>
<td><a href="/wiki/Bitwise_operation" title="Bitwise operation">bitwise</a> AND of two longs</td>
</tr>
<tr>
<td>lastore</td>
<td>50</td>
<td>0101 0000</td>
<td></td>
<td>arrayref, index, value →</td>
<td>store a long to an array</td>
</tr>
<tr>
<td>lcmp</td>
<td>94</td>
<td>1001 0100</td>
<td></td>
<td>value1, value2 → result</td>
<td>push 0 if the two longs are the same, 1 if value1 is greater than value2, -1 otherwise</td>
</tr>
<tr>
<td>lconst_0</td>
<td>09</td>
<td>0000 1001</td>
<td></td>
<td>→ 0L</td>
<td>push <i>0L</i> (the number <a href="/wiki/Zero" class="mw-redirect" title="Zero">zero</a> with type <i>long</i>) onto the stack</td>
</tr>
<tr>
<td>lconst_1</td>
<td>0a</td>
<td>0000 1010</td>
<td></td>
<td>→ 1L</td>
<td>push <i>1L</i> (the number <a href="/wiki/One" class="mw-redirect" title="One">one</a> with type <i>long</i>) onto the stack</td>
</tr>
<tr>
<td>ldc</td>
<td>12</td>
<td>0001 0010</td>
<td>1: index</td>
<td>→ value</td>
<td>push a constant <i>#index</i> from a constant pool (String, int, float, Class, java.lang.invoke.MethodType, or java.lang.invoke.MethodHandle) onto the stack</td>
</tr>
<tr>
<td>ldc_w</td>
<td>13</td>
<td>0001 0011</td>
<td>2: indexbyte1, indexbyte2</td>
<td>→ value</td>
<td>push a constant <i>#index</i> from a constant pool (String, int, float, Class, java.lang.invoke.MethodType, or java.lang.invoke.MethodHandle) onto the stack (wide <i>index</i> is constructed as <span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>ldc2_w</td>
<td>14</td>
<td>0001 0100</td>
<td>2: indexbyte1, indexbyte2</td>
<td>→ value</td>
<td>push a constant <i>#index</i> from a constant pool (double or long) onto the stack (wide <i>index</i> is constructed as <span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>ldiv</td>
<td>6d</td>
<td>0110 1101</td>
<td></td>
<td>value1, value2 → result</td>
<td>divide two longs</td>
</tr>
<tr>
<td>lload</td>
<td>16</td>
<td>0001 0110</td>
<td>1: index</td>
<td>→ value</td>
<td>load a long value from a local variable <i>#index</i></td>
</tr>
<tr>
<td>lload_0</td>
<td>1e</td>
<td>0001 1110</td>
<td></td>
<td>→ value</td>
<td>load a long value from a local variable 0</td>
</tr>
<tr>
<td>lload_1</td>
<td>1f</td>
<td>0001 1111</td>
<td></td>
<td>→ value</td>
<td>load a long value from a local variable 1</td>
</tr>
<tr>
<td>lload_2</td>
<td>20</td>
<td>0010 0000</td>
<td></td>
<td>→ value</td>
<td>load a long value from a local variable 2</td>
</tr>
<tr>
<td>lload_3</td>
<td>21</td>
<td>0010 0001</td>
<td></td>
<td>→ value</td>
<td>load a long value from a local variable 3</td>
</tr>
<tr>
<td>lmul</td>
<td>69</td>
<td>0110 1001</td>
<td></td>
<td>value1, value2 → result</td>
<td>multiply two longs</td>
</tr>
<tr>
<td>lneg</td>
<td>75</td>
<td>0111 0101</td>
<td></td>
<td>value → result</td>
<td>negate a long</td>
</tr>
<tr>
<td>lookupswitch</td>
<td>ab</td>
<td>1010 1011</td>
<td>4+: &lt;0–3 bytes padding&gt;, defaultbyte1, defaultbyte2, defaultbyte3, defaultbyte4, npairs1, npairs2, npairs3, npairs4, match-offset pairs...</td>
<td>key →</td>
<td>a target address is looked up from a table using a key and execution continues from the instruction at that address</td>
</tr>
<tr>
<td>lor</td>
<td>81</td>
<td>1000 0001</td>
<td></td>
<td>value1, value2 → result</td>
<td>bitwise OR of two longs</td>
</tr>
<tr>
<td>lrem</td>
<td>71</td>
<td>0111 0001</td>
<td></td>
<td>value1, value2 → result</td>
<td>remainder of division of two longs</td>
</tr>
<tr>
<td>lreturn</td>
<td>ad</td>
<td>1010 1101</td>
<td></td>
<td>value → [empty]</td>
<td>return a long value</td>
</tr>
<tr>
<td>lshl</td>
<td>79</td>
<td>0111 1001</td>
<td></td>
<td>value1, value2 → result</td>
<td>bitwise shift left of a long <i>value1</i> by int <i>value2</i> positions</td>
</tr>
<tr>
<td>lshr</td>
<td>7b</td>
<td>0111 1011</td>
<td></td>
<td>value1, value2 → result</td>
<td>bitwise shift right of a long <i>value1</i> by int <i>value2</i> positions</td>
</tr>
<tr>
<td>lstore</td>
<td>37</td>
<td>0011 0111</td>
<td>1: index</td>
<td>value →</td>
<td>store a long <i>value</i> in a local variable <i>#index</i></td>
</tr>
<tr>
<td>lstore_0</td>
<td>3f</td>
<td>0011 1111</td>
<td></td>
<td>value →</td>
<td>store a long <i>value</i> in a local variable 0</td>
</tr>
<tr>
<td>lstore_1</td>
<td>40</td>
<td>0100 0000</td>
<td></td>
<td>value →</td>
<td>store a long <i>value</i> in a local variable 1</td>
</tr>
<tr>
<td>lstore_2</td>
<td>41</td>
<td>0100 0001</td>
<td></td>
<td>value →</td>
<td>store a long <i>value</i> in a local variable 2</td>
</tr>
<tr>
<td>lstore_3</td>
<td>42</td>
<td>0100 0010</td>
<td></td>
<td>value →</td>
<td>store a long <i>value</i> in a local variable 3</td>
</tr>
<tr>
<td>lsub</td>
<td>65</td>
<td>0110 0101</td>
<td></td>
<td>value1, value2 → result</td>
<td>subtract two longs</td>
</tr>
<tr>
<td>lushr</td>
<td>7d</td>
<td>0111 1101</td>
<td></td>
<td>value1, value2 → result</td>
<td>bitwise shift right of a long <i>value1</i> by int <i>value2</i> positions, unsigned</td>
</tr>
<tr>
<td>lxor</td>
<td>83</td>
<td>1000 0011</td>
<td></td>
<td>value1, value2 → result</td>
<td>bitwise XOR of two longs</td>
</tr>
<tr>
<td>monitorenter</td>
<td>c2</td>
<td>1100 0010</td>
<td></td>
<td>objectref →</td>
<td>enter monitor for object ("grab the lock" – start of synchronized() section)</td>
</tr>
<tr>
<td>monitorexit</td>
<td>c3</td>
<td>1100 0011</td>
<td></td>
<td>objectref →</td>
<td>exit monitor for object ("release the lock" – end of synchronized() section)</td>
</tr>
<tr>
<td>multianewarray</td>
<td>c5</td>
<td>1100 0101</td>
<td>3: indexbyte1, indexbyte2, dimensions</td>
<td>count1, [count2,...] → arrayref</td>
<td>create a new array of <i>dimensions</i> dimensions with elements of type identified by class reference in constant pool <i>index</i> (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>); the sizes of each dimension is identified by <i>count1</i>, [<i>count2</i>, etc.]</td>
</tr>
<tr>
<td>new</td>
<td>bb</td>
<td>1011 1011</td>
<td>2: indexbyte1, indexbyte2</td>
<td>→ objectref</td>
<td>create new object of type identified by class reference in constant pool <i>index</i> (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>newarray</td>
<td>bc</td>
<td>1011 1100</td>
<td>1: atype</td>
<td>count → arrayref</td>
<td>create new array with <i>count</i> elements of primitive type identified by <i>atype</i></td>
</tr>
<tr>
<td>nop</td>
<td>00</td>
<td>0000 0000</td>
<td></td>
<td>[No change]</td>
<td>perform no operation</td>
</tr>
<tr>
<td>pop</td>
<td>57</td>
<td>0101 0111</td>
<td></td>
<td>value →</td>
<td>discard the top value on the stack</td>
</tr>
<tr>
<td>pop2</td>
<td>58</td>
<td>0101 1000</td>
<td></td>
<td>{value2, value1} →</td>
<td>discard the top two values on the stack (or one value, if it is a double or long)</td>
</tr>
<tr>
<td>putfield</td>
<td>b5</td>
<td>1011 0101</td>
<td>2: indexbyte1, indexbyte2</td>
<td>objectref, value →</td>
<td>set field to <i>value</i> in an object <i>objectref</i>, where the field is identified by a field reference <i>index</i> in constant pool (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>putstatic</td>
<td>b3</td>
<td>1011 0011</td>
<td>2: indexbyte1, indexbyte2</td>
<td>value →</td>
<td>set static field to <i>value</i> in a class, where the field is identified by a field reference <i>index</i> in constant pool (<span style="font-family: monospace, monospace;">indexbyte1 &lt;&lt; 8 + indexbyte2</span>)</td>
</tr>
<tr>
<td>ret</td>
<td>a9</td>
<td>1010 1001</td>
<td>1: index</td>
<td>[No change]</td>
<td>continue execution from address taken from a local variable <i>#index</i> (the asymmetry with jsr is intentional)</td>
</tr>
<tr>
<td>return</td>
<td>b1</td>
<td>1011 0001</td>
<td></td>
<td>→ [empty]</td>
<td>return void from method</td>
</tr>
<tr>
<td>saload</td>
<td>35</td>
<td>0011 0101</td>
<td></td>
<td>arrayref, index → value</td>
<td>load short from array</td>
</tr>
<tr>
<td>sastore</td>
<td>56</td>
<td>0101 0110</td>
<td></td>
<td>arrayref, index, value →</td>
<td>store short to array</td>
</tr>
<tr>
<td>sipush</td>
<td>11</td>
<td>0001 0001</td>
<td>2: byte1, byte2</td>
<td>→ value</td>
<td>push a short onto the stack</td>
</tr>
<tr>
<td>swap</td>
<td>5f</td>
<td>0101 1111</td>
<td></td>
<td>value2, value1 → value1, value2</td>
<td>swaps two top words on the stack (note that value1 and value2 must not be double or long)</td>
</tr>
<tr>
<td>tableswitch</td>
<td>aa</td>
<td>1010 1010</td>
<td>4+: [0–3 bytes padding], defaultbyte1, defaultbyte2, defaultbyte3, defaultbyte4, lowbyte1, lowbyte2, lowbyte3, lowbyte4, highbyte1, highbyte2, highbyte3, highbyte4, jump offsets...</td>
<td>index →</td>
<td>continue execution from an address in the table at offset <i>index</i></td>
</tr>
<tr>
<td>wide</td>
<td>c4</td>
<td>1100 0100</td>
<td>3/5: opcode, indexbyte1, indexbyte2<br>
or<br>
iinc, indexbyte1, indexbyte2, countbyte1, countbyte2</td>
<td>[same as for corresponding instructions]</td>
<td>execute <i>opcode</i>, where <i>opcode</i> is either iload, fload, aload, lload, dload, istore, fstore, astore, lstore, dstore, or ret, but assume the <i>index</i> is 16 bit; or execute iinc, where the <i>index</i> is 16 bits and the constant to increment by is a signed 16 bit short</td>
</tr>
<tr>
<td><i>(no name)</i></td>
<td>cb-fd</td>
<td></td>
<td></td>
<td></td>
<td>these values are currently unassigned for opcodes and are reserved for future use</td>
</tr>
</tbody><tfoot></tfoot></table>"""

import binascii
import pandas as pd

# Prepare java opcodes
opcodes_full = pd.read_html(h)[0]
opcodes = opcodes_full.copy()[['Mnemonic', 'Opcode (in hexadecimal)', 'Opcode (in binary)']]
opcodes = opcodes.rename(columns={'Opcode (in hexadecimal)':'hex', 'Opcode (in binary)': 'bin'})
opdict = opcodes.set_index('hex').to_dict()['Mnemonic']

# Read Java class
with open('SimpleClass7.class', 'rb') as f:
    # Slurp the whole file and efficiently convert it to hex all at once
    hexdata = binascii.hexlify(f.read()).decode('ascii')
n = 2
hexdata = [hexdata[i:i+n] for i in range(0, len(hexdata), n)]

MAGIC_NUMBER_L = 4
offset = 0
cafebabe = hexdata[offset:MAGIC_NUMBER_L]
offset += MAGIC_NUMBER_L

#VERSION_L = 4
#version = hexdata[offset:offset + VERSION_L]
#offset += VERSION_L
#
#CONSTANT_POOL_COUNT_L = 2
#constant_pool_count = hexdata[offset:offset + CONSTANT_POOL_COUNT_L]
#offset += CONSTANT_POOL_COUNT_L
#
#int_constant_pool_count = int(''.join(constant_pool_count), 16)
#constants = hexdata[offset: offset + int_constant_pool_count]
#offset += int_constant_pool_count





#ACCESS_FLAGS_L = 2
#access_flags = hexdata[offset: offset + ACCESS_FLAGS_L]
#offset += ACCESS_FLAGS_L
#
#THIS_CLASS_L = 2
#this_class = hexdata[offset: offset + THIS_CLASS_L]
#offset += THIS_CLASS_L
#
#SUPER_CLASS_L = 2
#super_class = hexdata[offset: offset + SUPER_CLASS_L]
#offset += SUPER_CLASS_L
#
#INTERFACES_COUNT_L = 2
#interfaces_count = hexdata[offset: offset + INTERFACES_COUNT_L]
#offset += INTERFACES_COUNT_L
#
