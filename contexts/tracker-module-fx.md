This is a list of all effects that can be used in the **Pattern Editor**.

Commands that go into the effect column use **uppercase** effect letters (e.g. `G05`), while effects that go into the volume column use **lowercase** effect letters (e.g. `g05`).

The MOD and XM formats share a similar set of effects, with XM offering more effects than MOD. The S3M, IT, and MPTM formats share a different set of effects, of which many are compatible to those from the MOD and XM formats but have different letters (e.g. `F75` in MOD turns into `T75` in S3M).

Currently, some effects, in particular those related to volume and panning, only directly affect samples, not instrument plugins (but their effect may be observed by a plugin through **MIDI macros**). Since this might change in the future, such effects should be avoided on channels that make use of instrument plugins.

The behaviour of some effects is influenced by the **Compatible Playback** settings.

## Effect Layout

`**A**xy`: Effect letter <br />
`A**xy**`: Parameter

An effect command consists of one character (the **“effect letter”**) followed by the value that will be applied (the **“parameter”**), which is a **hexadecimal** number in the effect column and a **decimal** number in the volume column.

If the notation for a parameter is given as **`xx`**, it means that a 2-digit hexadecimal number is expected. For this notation, 62h would be equal to 98, 29h would be equal to 41, and E8h would be equal to 232.

If the notation is **`xy`**, it means that for `x` and `y` each, a 1-digit hexadecimal number is expected. For this notation, each of the digits in 62h would be read separately — as 6 and 2. Similarly, 29h would mean 2 and 9, and E8h would mean 14 and 8.

If you do not wish to manually convert decimal to hexadecimal and vice versa, you can double-click on a pattern cell or press the <kbd>▤ Application</kbd> key (by default) to open the **Note Properties** dialog. From there, you can adjust the effect parameters using sliders and see their meaning.

Note that the parameter range of some commands is limited, such as the XM command <abbr title="Set Volume">`Cxx`</abbr>, whose maximum value is 40h (64). Entering out-of-range values is not recommended as they might be interpreted differently by non-conforming players.

## Frequency Units

If **Linear Frequency Slides** are enabled, each unit of a pitch sliding effect (e.g. <abbr title="Portamento Down">`Exx`</abbr> or <abbr title="Tone Portamento">`Gxx`</abbr> in the IT / MPTM format) is equal to <sup>1</sup>⁄<sub>16</sub> of a semitone. Extra fine (e.g. <abbr title="Extra Fine Portamento Down">`EEx`</abbr> or <abbr title="Extra Fine Portamento Up">`FEx`</abbr>) units are equal to <sup>1</sup>⁄<sub>64</sub> of a semitone.

Otherwise, the unit of a pitch sliding effect is one *period*, a metric inverse to frequency. This means that the lower the current note, the smaller the effect of one pitch slide unit, and the higher the note, the bigger the effect is. The period of a frequency can be calculated by dividing 3,546,895 by the frequency (3,579,364 in the XM format).

Linear Frequency Slides are not available in the MOD and S3M formats.

## Common Effect Parameters

Most effect parameters follow a simple scheme, but some effects also use a look-up table for their parameters. Depending on the effect, different parameters might do completely different things. Some common look-up table effects are described below.

### Waveform Types

Some oscillator effects (namely Vibrato, Tremolo and Panbrello) use preset oscillator waveforms. They can be changed using special commands; the possible parameters of these commands are listed here.

#### S3M / IT / MPTM Formats

| Parameter | Waveform |
|---|---|
| **0** (default) | Sine (retrigger) |
| **1** | Sawtooth (retrigger) |
| **2** | Square (retrigger) |
| **3** | Random (retrigger) |

#### MOD / XM Formats

| Parameter | Waveform | Parameter | Waveform |
|---|---|---|---|
| **0** (default) | Sine (retrigger) | **4** | Sine (continue) |
| **1** | Sawtooth (retrigger) | **5** | Sawtooth (continue) |
| **2** | Square (retrigger) | **6** | Square (continue) |
| **3** | Random<sup>†</sup> (retrigger) | **7** | Random<sup>†</sup> (continue) |

**Retrigger** means that the oscillator waveform is played from its starting point when a new note is played.
**Continue** means that the waveform continues at its last playback position when a new note is played. This waveform type is exclusive to the MOD and XM formats.

Every oscillator waveform is 64 points long, and the speed parameter denotes by how many points per tick the play position is advanced. So at a vibrato speed of 2, the vibrato waveform repeats after 32 ticks.

<sup>†</sup> The **Random** waveforms are not supported by ProTracker and FastTracker. While they are supported by some MOD / XM players, they should be avoided.

### Retrigger Volume

The Retrigger command (`Rxy` in the XM format, `Qxy` in the S3M / IT / MPTM formats) does not only retrigger the note every `y` ticks, but also changes the note volume depending on the `x` value.

The following table explains the meaning of every possible `x` parameter:

| Parameter | Effect | Parameter | Effect |
|---|---|---|---|
| **0** | (XM) Last `x` value <br /> (S3M / IT / MPTM) No volume change | **8** | No volume change |
| **1** | Volume - 1 | **9** | Volume + 1 |
| **2** | Volume - 2 | **A** | Volume + 2 |
| **3** | Volume - 4 | **B** | Volume + 4 |
| **4** | Volume - 8 | **C** | Volume + 8 |
| **5** | Volume - 16 | **D** | Volume + 16 |
| **6** | Volume × ⅔ | **E** | Volume × 1.5 |
| **7** | Volume × ½ | **F** | Volume × 2 |

### Sound Control

The sound control effect (`X9x` in the XM format, `S9x` in the S3M / IT / MPTM formats) can be used to control various aspects of sound playback. Possible parameters are listed here.

> **Warning:** Using any of these commands outside of the MPTM format (except <abbr title="Surround On">`S91`</abbr> which is native to only the IT and MPTM formats) is considered a ModPlug hack.

| Parameter | Name | Description |
|---|---|---|
| **0** | Surround Off | Disables surround playback on the current channel. <br /> This should only be used when using Quad Surround Panning. <br /> To keep compatibility with other trackers, a normal panning effect should be used in Center Surround mode. |
| **1** | Surround On | Enables surround playback on the current channel. <br /> When using stereo playback, the right channel of a sample is played with inversed phase (Pro Logic Surround). When using quad playback, the rear channels are used for playing this channel. |
| **8** | Reverb Off | Disables Reverb on the current channel. |
| **9** | Reverb On | > **Warning:** As there is no per-song configuration for reverb available, use of this command is discouraged. Use a reverb plugin such as **I3DL2Reverb** or **WavesReverb** instead. <br /> Enables Reverb on the current channel. <br /> The reverb parameters from the **DSP Setup** are used. |
| **A** | Center Surround | Sets the surround mode to Center Surround for all channels. This is the default mode. <br /> The <abbr title="Surround On">`S91`</abbr> command will place the channel in the center of the rear channels. Any panning command will bring it back to the front channels. |
| **B** | Quad Surround | Sets the surround mode to Quad Surround for all channels. <br /> In this mode, panning commands can adjust the position of the rear channels. <br /> Switching between the front and rear channels can only be done by using the <abbr title="Surround On">`S91`</abbr> and <abbr title="Surround Off">`S90`</abbr> commands. |
| **C** | Global Filters | Sets filter mode to Global on all channels (Impulse Tracker behaviour). <br /> In this mode, when resonant filters are enabled with a **MIDI macro** effect, they will stay active until explicitly disabled by setting the cutoff frequency to the maximum (`Z7F`), and the resonance to the minimum (`Z80`). |
| **D** | Local Filters | Sets filter mode to Local on all channels. <br /> In this mode, the resonant filter will only affect the current note and will revert when a new note is played. |
| **E** | Play Forward | Forces the current sample to play forward. |
| **F** | Play Backward | Forces the current sample to play backward. |

## MOD Effect Commands

### Effect Column

The MOD format only allows use of the effect column; there are no volume column commands.

All parameter values are **hexadecimal**.

| <abbr title="Effect">Eff</abbr> | Name | <abbr title="Memory">Mem</abbr><sup>†</sup> | Description | Category |
|---|---|---|---|---|
| **`0xy`** | Arpeggio | No | Plays an arpeggiation of three notes in one row, cycling between the current note, current note + `x` semitones, and current note + `y` semitones. | Pitch |
| **`1xx`** | Portamento Up | No | Increases current note pitch by `xx` **units** on every tick of the row except the first. | Pitch |
| **`2xx`** | Portamento Down | No | Decreases current note pitch by `xx` **units** on every tick of the row except the first. | Pitch |
| **`3xx`** | Tone Portamento | Yes | Slides the pitch of the previous note towards the current note by `xx` **units** on every tick of the row except the first. | Pitch |
| **`4xy`** | Vibrato | Yes | Executes vibrato with speed `x` and depth `y` on the current note. <br /> Modulates with selected vibrato waveform (see the **Waveform Types table** for more details). | Pitch |
| **`5xy`** | Volume Slide + Tone Portamento | <abbr title="500 will call Tone Portamento memory">No</abbr> | Functions like <abbr title="Volume Slide">`Axy`</abbr> with <abbr title="Tone Portamento memory">`300`</abbr>. <br /> Parameters are used like `Axy`. | Miscellaneous |
| **`6xy`** | Volume Slide + Vibrato | <abbr title="600 will call Vibrato memory">No</abbr> | Functions like <abbr title="Volume Slide">`Axy`</abbr> with <abbr title="Vibrato memory">`400`</abbr>. <br /> Parameters are used like `Axy`. | Miscellaneous |
| **`7xy`** | Tremolo | Yes | Executes tremolo with speed `x` and depth `y` on the current note. <br /> Modulates with selected tremolo waveform (see the **Waveform Types table** for more details). | Volume |
| **`8xx`** | Set Panning | — | Sets the current channel's panning position. <br /> Ranges from 00h (left) to FFh (right). | Panning |
| **`9xx`** | Sample Offset | Yes | Starts playing the current sample from position `xx` × 256, instead of position 0.  <br /> Ineffective if there is no note in the same pattern cell. | Miscellaneous |
| **`Axy`** | Volume Slide | No | Slides the current note volume up or down. <br /> * **`A0y`** **decreases** note volume by `y` units on every tick of the row except the first. <br /> * **`Ax0`** **increases** note volume by `x` units on every tick of the row except the first. | Volume |
| **`Bxx`** | Position Jump | — | Causes playback to jump to pattern position `xx`. <br />`B00` would restart a song from the beginning (first pattern in the Order List). <br /> If <abbr title="Pattern Break">`Dxx`</abbr> is on the same row, the pattern specified by `Bxx` will be the pattern `Dxx` jumps in. <br /> Ranges from 00h to 7Fh (127; maximum amount of patterns for the MOD format). | Global *(Pattern)* |
| **`Cxx`** | Set Volume | — | Sets the current note volume to `xx`. <br /> Ranges from 00h (off) to 40h (full). | Volume |
| **`Dxx`** | Pattern Break | — | Jumps to row `xx` of the next pattern in the Order List. <br /> If the current pattern is the last pattern in the Order List, `Dxx` will jump to row `xx` of the first pattern. <br /> If <abbr title="Position Jump">`Bxx`</abbr> is on the same row, the pattern specified by `Bxx` will be the pattern `Dxx` jumps in. <br /> Ranges from 00h to 3Fh (64; maximum amount of rows for each pattern in the MOD format). | Global *(Pattern)* |
| **`E0x`** | Set Filter | — | Configures the Amiga's LED lowpass filter. <br /> * **`E00`** **enables** emulation of the lowpass filter. <br /> * **`E01`** **disables** emulation of the lowpass filter. <br /> Enabling the filter makes the sound output more muffled and is not recommended. <br /> Using this effect is only recommended to explicitly disable the filter for environments where it might not be disabled by default (such as a real Amiga system). <br />OpenMPT only emulates the lowpass filter if the **Amiga resampler** is enabled in the Mixer settings. | Miscellaneous |
| **`E1x`** | Fine Portamento Up | No | Similar to <abbr title="Portamento Up">`1xx`</abbr>, but only applies on the first tick of the row. | Pitch |
| **`E2x`** | Fine Portamento Down | No | Similar to <abbr title="Portamento Down">`2xx`</abbr>, but only applies on the first tick of the row. | Pitch |
| **`E3x`** | Glissando Control | — | > **Warning:** This effect is not widely supported and behaves quirky in OpenMPT. <br /> Configures whether tone portamento effects slide by semitones or not. <br /> * **`E30` disables** glissando. <br /> * **`E31` enables** glissando. | Pitch |
| **`E4x`** | Set Vibrato Waveform | — | Sets the waveform of future Vibrato effects (see the **Waveform Types table** for more details). | Pitch |
| **`E5x`** | Set Finetune | — | Overrides the finetune value for the currently playing note. <br /> Functions similarly to the same setting in the Sample Editor but is only applied temporarily. <br /> This command only works with a note next to it. | Pitch |
| **`E60`** | Pattern Loop Start | — | Marks the current row position to be used as the start of a pattern loop. | Global *(Pattern)* |
| **`E6x`** | Pattern Loop | — | Each time this command is reached, jumps to the row marked by <abbr title="Pattern Loop Start">`E60`</abbr> until `x` jumps have occured in total. <br /> If `E6x` is used in a pattern with no `E60` effect, `E6x` will use the row position marked by any previous `E60` effect. <br /> Pattern loops cannot span multiple patterns. <br /> Ranges from 1h to Fh. | Global *(Pattern)* |
| **`E7x`** | Set Tremolo Waveform | — | Sets the waveform of future Tremolo effects (see the **Waveform Types table** for more details). | Volume |
| **`E8x`** | Set Panning | — | *<abbr title="Set Panning">`8xx`</abbr> is a much finer panning effect.* <br /> Sets the current channel's panning position. <br /> Ranges from 0h (left) to Fh (right). | Panning |
| **`E9x`** | Retrigger | No | Retriggers the current note every `x` ticks. <br /> This effect works with parameters greater than the current Speed setting if the row after it also contains an `E9x` effect. | Miscellaneous |
| **`EAx`** | Fine Volume Slide Up | No | Similar to <abbr title="Volume Slide Up">`Ax0`</abbr>, but only applies on the first tick of the row. | Volume |
| **`EBx`** | Fine Volume Slide Down | No | Similar to <abbr title="Volume Slide Down">`A0y`</abbr>, but only applies on the first tick of the row. | Volume |
| **`ECx`** | Note Cut | — | Sets note volume to 0 after `x` ticks. <br /> If `x` is greater than or equal to the current module Speed, this command is ignored. | Miscellaneous |
| **`EDx`** | Note Delay | — | Delays the note or instrument change in the current pattern cell by `x` ticks. <br /> If `x` is greater than or equal to the current module Speed, the current pattern cell's contents are never played. | Miscellaneous |
| **`EEx`** | Pattern Delay | — | Repeats the current row `x` times. <br /> Notes are not retriggered on every repetition, but effects are still processed. <br /> If multiple `EEx` commands are found on the same row, only the rightmost is considered. | Global *(Pattern)* |
| **`EFx`** | Invert Loop | — | > **Warning:** This effect permanently modifies the module file when encountered during playback. <br /> * **`EFx`**, when used with a looped sample, goes through the sample loop and inverts all sampling points (i.e. changes the sign) one by one at speed `x`. <br /> * **`EF0`** cancels `EFx`. <br /> Samples modified by this effect cannot be recovered automatically (e.g. no undo point is created). | Miscellaneous |
| **`Fxx`** | Set Speed / Tempo | — | > **Warning:** Avoid using 20h or 00h as parameters. <br /> * Sets the module **Speed** (ticks per row) if `xx` is less than 20h. <br /> * Sets the module **Tempo** if `xx` greater than or equal to 20h. <br /> Some players (including old OpenMPT versions) differ in their interpretations of `F20`. <br /> `F00` does nothing in OpenMPT, but some players stop the song when they encounter it. | Global *(Timing)* |

**<sup>†</sup> Effect Memory**: <br />
Assuming that all available parameters for a given effect (e.g. `xx`, `xy`, `x`, or `y`) are equivalent to 0:
* **No** means that the command does nothing.
* **Yes** means that the effect calls its own parameter memory. <br /> For example, if the effect <abbr title="Vibrato with speed 8 and depth 2">`482`</abbr> is followed by the effect <abbr title="Vibrato memory">`400`</abbr> on a subsequent row, the `400` effect recalls the effect parameter 82h.
* **—** means that the value has no special meaning. <br /> For example, <abbr title="Set Volume">`C00`</abbr> sets note volume to 0, <abbr title="Set Panning">`800`</abbr> sets the channel's panning position to hard left, <abbr title="Position Jump">`B00`</abbr> jumps to the first pattern, etc.

## XM Effect Commands

Fasttracker II's XM format uses an extended version of the MOD command set.

As Fasttracker II was a rather buggy program, many effect commands may behave in a quirky way. Always enable the default **FT2 compatible playback settings** for the best possible emulation of those quirks.

### Effect Column

Some effects and features mentioned here are not actually part of the original XM format specifications. They will be labeled **ModPlug hacks**, as they are not compatible with Fasttracker II. Use of these effects in the XM format is strongly discouraged; if you wish to use them, you should use a different format with equivalent, natively implemented effects.

All parameter values are **hexadecimal**.

| <abbr title="Effect">Eff</abbr> | Name | <abbr title="Memory">Mem</abbr><sup>†</sup> | Description | Category |
|---|---|---|---|---|
| **`0xy`** | Arpeggio | No | Plays an arpeggiation of three notes in one row, cycling between the current note, current note + `x` semitones, and current note + `y` semitones. | Pitch |
| **`1xx`** | Portamento Up | Yes | Increases current note pitch by `xx` **units** on every tick of the row except the first. | Pitch |
| **`2xx`** | Portamento Down | Yes | Decreases current note pitch by `xx` **units** on every tick of the row except the first. | Pitch |
| **`3xx`** | Tone Portamento | Yes | Slides the pitch of the previous note towards the current note by `xx` **units** on every tick of the row except the first. | Pitch |
| **`4xy`** | Vibrato | Yes | Executes vibrato with speed `x` and depth `y` on the current note. <br /> Modulates with selected vibrato waveform (see the **Waveform Types table** for more details). | Pitch |
| **`5xy`** | Volume Slide + Tone Portamento | Yes | Functions like <abbr title="Volume Slide">`Axy`</abbr> with <abbr title="Tone Portamento memory">`300`</abbr>. <br /> Parameters are used like `Axy`. | Miscellaneous |
| **`6xy`** | Volume Slide + Vibrato | Yes | Functions like <abbr title="Volume Slide">`Axy`</abbr> with <abbr title="Vibrato memory">`400`</abbr>. <br /> Parameters are used like `Axy`. | Miscellaneous |
| **`7xy`** | Tremolo | Yes | Executes tremolo with speed `x` and depth `y` on the current note. <br /> Modulates with selected tremolo waveform (see the **Waveform Types table** for more details). | Volume |
| **`8xx`** | Set Panning | — | Sets the current sample's panning position. <br /> As every sample has an enforced default panning, this setting is reset by any future entries in the instrument column. <br /> Ranges from 00h (left) to FFh (right). | Panning |
| **`9xx`** | Sample Offset | Yes | Starts playing the current sample from position `xx` × 256, instead of position 0. <br /> Ineffective if there is no note in the same pattern cell. | Miscellaneous |
| **`Axy`** | Volume Slide | Yes | Slides the current note volume up or down. <br /> * **`A0y`** **decreases** note volume by `y` units on every tick of the row except the first. <br /> * **`Ax0`** **increases** note volume by `x` units on every tick of the row except the first. | Volume |
| **`Bxx`** | Position Jump | — | Causes playback to jump to pattern position `xx`. <br />`B00` would restart a song from the beginning (first pattern in the Order List). <br /> If <abbr title="Pattern Break">`Dxx`</abbr> is on the same row and **to the right** of `Bxx`, the pattern specified by `Bxx` will be the pattern `Dxx` jumps in. | Global *(Pattern)* |
| **`Cxx`** | Set Volume | — | Sets the current note volume to `xx`. <br /> Ranges from 00h (off) to 40h (full). | Volume |
| **`Dxx`** | Pattern Break | — | > **Warning:** To maintain compatibility with Fasttracker II, you should not jump past row 3Fh (63). <br /> Jumps to row `xx` of the next pattern in the Order List. <br /> If the current pattern is the last pattern in the Order List, `Dxx` will jump to row `xx` of the first pattern. <br /> If <abbr title="Position Jump">`Bxx`</abbr> is on the same row and **to the left** of `Bxx`, the pattern specified by `Bxx` will be the pattern `Dxx` jumps in. | Global *(Pattern)* |
| **`E1x`** | Fine Portamento Up | Yes | Similar to <abbr title="Portamento Up">`1xx`</abbr>, but only applies on the first tick of the row. | Pitch |
| **`E2x`** | Fine Portamento Down | Yes | Similar to <abbr title="Portamento Down">`2xx`</abbr>, but only applies on the first tick of the row. | Pitch |
| **`E3x`** | Glissando Control | — | > **Warning:** This effect is not widely supported and behaves quirky in OpenMPT. <br /> Configures whether tone portamento effects slide by semitones or not. <br /> * **`E30`** **disables** glissando. <br /> * **`E31`** **enables** glissando. | Pitch |
| **`E4x`** | Set Vibrato Waveform | — | Sets the waveform of future Vibrato effects (see the **Waveform Types table** for more details). | Pitch |
| **`E5x`** | Set Finetune | — | Overrides the finetune value for the currently playing note. <br /> Functions similarly to the same setting in the Sample Editor but is only applied temporarily. <br /> This command only works with a note next to it. | Pitch |
| **`E60`** | Pattern Loop Start | — | > **Warning:** A Fasttracker II bug makes use of this command non-trivial. <br /> Marks the current row position to be used as the start of a pattern loop. <br /> > **Warning:** When `E60` is used on pattern row `x`, the following pattern also starts from row `x` instead of row 0. <br /> This can be circumvented by using a <abbr title="Pattern Break">`D00`</abbr> command on the last row of the same pattern. | Global *(Pattern)* |
| **`E6x`** | Pattern Loop | — | Each time this command is reached, jumps to the row marked by <abbr title="Pattern Loop Start">`E60`</abbr> until `x` jumps have occured in total. <br /> If `E6x` is used in a pattern with no `E60` effect, `E6x` will use the row position marked by any previous `E60` effect. <br /> Pattern loops cannot span multiple patterns. <br /> Ranges from 1h to Fh. | Global *(Pattern)* |
| **`E7x`** | Set Tremolo Waveform | — | Sets the waveform of future Tremolo effects (see the **Waveform Types table** for more details). | Volume |
| **`E8x`** | Set Panning | — | *<abbr title="Set Panning">`8xx`</abbr> is a much finer panning effect.* <br /> Sets the current channel's panning position. <br /> Ranges from 0h (left) to Fh (right). | Panning |
| **`E9x`** | Retrigger | No | Retriggers the current note every `x` ticks. <br /> This effect works with parameters greater than the current Speed setting if the row after it also contains an `E9x` effect. | Miscellaneous |
| **`EAx`** | Fine Volume Slide Up | Yes | Similar to <abbr title="Volume Slide Up">`Ax0`</abbr>, but only applies on the first tick of the row. | Volume |
| **`EBx`** | Fine Volume Slide Down | Yes | Similar to <abbr title="Volume Slide Down">`A0y`</abbr>, but only applies on the first tick of the row. | Volume |
| **`ECx`** | Note Cut | — | Sets note volume to 0 after `x` ticks. <br /> If `x` is greater than or equal to the current module Speed, this command is ignored. | Miscellaneous |
| **`EDx`** | Note Delay | — | > **Warning:** This command is very buggy (e.g. portamento effects next to a note delay are ignored). You should not rely on these bugs to be emulated by other players. <br /> Delays the note or instrument change in the current pattern cell by `x` ticks. <br /> If `x` is greater than or equal to the current module Speed, the current pattern cell's contents are never played. | Miscellaneous |
| **`EEx`** | Pattern Delay | — | Repeats the current row `x` times. <br /> Notes are not retriggered on every repetition, but effects are still processed. <br /> If multiple `EEx` commands are found on the same row, only the rightmost is considered. | Global *(Pattern)* |
| **`EFx`** | Set Active Macro | — | > **Warning:** This effect is a ModPlug hack. <br /> Selects the active **parametered macro** for the current channel. | Miscellaneous |
| **`Fxx`** | Set Speed / Tempo | — | > **Warning:** Avoid using 00h as a parameter. <br /> * Sets the module **Speed** (ticks per row) if `xx` is less than 20h. <br /> * Sets the module **Tempo** if `xx` greater than or equal to 20h. <br /> In OpenMPT and Fasttracker II, `F00` sets the Speed to 65535 ticks per row, but in other players it may stop the song entirely, or simply do nothing. | Global *(Timing)* |
| **`Gxx`** | Set Global Volume | — | Sets the global volume. <br /> Ranges from 00h (off) to 40h (full). | Volume |
| **`Hxy`** | Global Volume Slide | Yes | Similar to <abbr title="Volume Slide">`Axy`</abbr>, but applies to the global volume. | Volume |
| **`Kxx`** | Key Off | — | > **Warning:** Avoid using 00h as a parameter; it interferes with other entries (e.g. notes, instruments) in the same pattern cell. <br /> Triggers a Note Off command after `xx` ticks. | Miscellaneous |
| **`Lxx`** | Set Envelope Position | — | Sets the volume envelope playback position to `xx` ticks. <br /> If the volume envelope’s sustain point is enabled, the panning envelope position is also changed. | Volume |
| **`Pxy`** | Panning Slide | Yes | Slides the current sample's panning position left or right. <br /> * **`P0y`** slides the panning to the **left** by `y` units on the first tick of the row. <br /> * **`Px0`** slides the panning to the **right** by `x` units on the first tick of the row. | Panning |
| **`Rxy`** | Retrigger | Yes | > **Warning:** This command is very buggy (e.g. if a volume command is in the same pattern cell as `Rxy`, it will skip some ticks). <br /> Retriggers the current note every `y` ticks and changes the volume based on the `x` value (see the **Retrigger Volume table** for more details). | Miscellaneous |
| **`Txy`** | Tremor | Yes | Rapidly switches the sample volume on and off on every tick of the row except the first. <br /> Volume is **on** for `x` + 1 ticks and **off** for `y` + 1 ticks. <br /> For instrument plugins > **Warning:** (ModPlug hack), this command sends note-on and note-off messages instead of modifying the volume. | Volume |
| **`X1x`** | Extra Fine Portamento Up | Yes | Similar to <abbr title="Fine Portamento Up">`E1x`</abbr>, but with 4 times the precision. | Pitch |
| **`X2x`** | Extra Fine Portamento Down | Yes | Similar to <abbr title="Fine Portamento Down">`E2x`</abbr>, but with 4 times the precision. | Pitch |
| **`X5x`** | Set Panbrello Waveform | — | > **Warning:** This effect is a ModPlug hack. <br /> Sets the waveform of future Panbrello effects (see the **Waveform Types table** for more details). | Panning |
| **`X6x`** | Fine Pattern Delay | — | > **Warning:** This effect is a ModPlug hack. <br /> Extends the current row by `x` ticks. <br /> If multiple `X6x` commands are found on the same row, the sum of their parameters is used. | Global *(Pattern)* |
| **`X9x`** | Sound Control | — | > **Warning:** This effect is a ModPlug hack. <br /> Executes a sound control command (see the **Sound Control table** for more details). | Miscellaneous |
| **`XAx`** | High Offset | — | > **Warning:** This effect is a ModPlug hack. <br /> Sets the high offset for future <abbr title="Sample Offset">`9xx`</abbr> commands. <br /> `x` × 65536 (10000h) is added to all offset effects that follow this command. | Miscellaneous |
| **`Yxy`** | Panbrello | Yes | > **Warning:** This effect is a ModPlug hack. <br /> Executes Panbrello with speed `x` and depth `y` on the current note. <br /> Modulates with selected Panbrello waveform (see the **Waveform Types table** for more details). | Panning |
| **`Zxx`** | MIDI Macro | — | > **Warning:** This effect is a ModPlug hack. <br /> Executes a **MIDI Macro**. | Miscellaneous |
| **`\xx`** | Smooth MIDI Macro | — | > **Warning:** This effect is a ModPlug hack. <br /> Executes an interpolated MIDI Macro. | Miscellaneous |

### Volume Column

The following commands can be entered into the effect column.

All parameter values are **decimal**.

| <abbr title="Effect">Eff</abbr> | Name | <abbr title="Memory">Mem</abbr><sup>†</sup> | Description | Category |
|---|---|---|---|---|
| **`axx`** | Fine Volume Slide Up | No | Functions like <abbr title="Fine Volume Slide Up">`EAx`</abbr> (slides the volume up `xx` units on the first tick). | Volume |
| **`bxx`** | Fine Volume Slide Down | No | Functions like <abbr title="Fine Volume Slide Down">`EBx`</abbr> (slides the volume down `xx` units on the first tick). | Volume |
| **`cxx`** | Volume Slide Up | No | Functions like <abbr title="Volume Slide Up">`Ax0`</abbr> (slides the volume up `xx` units on all ticks except the first). | Volume |
| **`dxx`** | Volume Slide Down | No | Functions like <abbr title="Volume Slide Down">`A0y`</abbr> (slides the volume down `xx` units on all ticks except the first). | Volume |
| **`gxx`** | Tone Portamento | Yes | Functions like <abbr title="Tone Portamento">`3xx`</abbr> (pitch-bends from the previous note to the current note). <br /> Compared to `3xx`, parameters are 16 times more coarse (e.g. `g01` = <abbr title="Tone Portamento with speed 16 (10h)">`310`</abbr>). <br /> Combining the effect with `3xx` will double the effect parameter (e.g. `g01` would act like `g02`) and ignores the `3xx` command. <br /> Ineffective if <abbr title="Note Delay">`EDx`</abbr> is in the same pattern cell. | Pitch |
| **`hxx`** | Vibrato Depth | Yes | Executes vibrato with depth `xx` and speed from the last <abbr title="Vibrato">`4xy`</abbr> or <abbr title="Vibrato Speed">`u0x`</abbr> command. | Pitch |
| **`lxx`** | Panning Slide Left | No | Functions like <abbr title="Panning Slide Left">`P0y`</abbr> (slides the panning left by `xx` units). | Panning |
| **`pxx`** | Set Panning | No | Sets the current channel's panning position. <br /> This effect only accepts 4-bit parameters (with 16 distinct values); when saving the file, parameters that are not multiples of 4 will be rounded down to a multiple of 4 (e.g. 55 will save as 52 and 5 will save as 4). <br /> Ranges from 0 (left) to 64 (right). | Panning |
| **`rxx`** | Panning Slide Right | No | Functions like <abbr title="Panning Slide Right">`Px0`</abbr> (slides the panning right by `xx` units). | Panning |
| **`uxx`** | Vibrato Speed | No | Sets the vibrato speed to `xx`, but does not execute a vibrato. | Pitch |
| **`vxx`** | Set Volume | No | Sets the current note volume to `xx`. <br /> The behaviour of this command when sent to instrument plugins can be configured in the **Instrument Editor** > **Warning:** (ModPlug hack). <br /> Ranges from 0 (off) to 64 (full). | Volume |

**<sup>†</sup> Effect Memory**: <br />
Assuming that all available parameters for a given effect (e.g. `xx`, `xy`, `x`, or `y`) are equivalent to 0:
* **No** means that the command does nothing.
* **Yes** means that the effect calls its own parameter memory. <br /> For example, if the effect <abbr title="Vibrato with speed 8 and depth 2">`482`</abbr> is followed by the effect <abbr title="Vibrato memory">`400`</abbr> on a subsequent row, the `400` effect recalls the effect parameter 82h.
* **—** means that the value has no special meaning. <br /> For example, <abbr title="Set Volume">`C00`</abbr> sets note volume to 0, <abbr title="Set Panning">`800`</abbr> sets the channel's panning position to hard left, <abbr title="Position Jump">`B00`</abbr> jumps to the first pattern, etc.

## S3M Effect Commands

Scream Tracker′s S3M format uses a command set that is entirely different from the MOD and XM formats'. Its use of the volume column is very limited.

The format was soon extended by other programs and players to support more effect commands (e.g. 7-Bit panning, panning slides, channel volume, etc.). Though these are also supported by OpenMPT, they are not original Scream Tracker 3 commands, and thus will be marked as such below.

### Effect Column

The following commands can be entered into the effect column.

All parameter values are **hexadecimal**.

| <abbr title="Effect">Eff</abbr> | Name | <abbr title="Memory">Mem</abbr><sup>†</sup> | Description | Category |
|---|---|---|---|---|
| **`Axx`** | Set Speed | No | Sets the module Speed (ticks per row). | Global *(Timing)* |
| **`Bxx`** | Position Jump | — | Causes playback to jump to pattern position `xx`. <br /> `B00` would restart a song from the beginning (first pattern in the Order List). <br /> If <abbr title="Pattern Break">`Cxx`</abbr> is on the same row, the pattern specified by `Bxx` will be the pattern `Cxx` jumps in. | Global *(Pattern)* |
| **`Cxx`** | Pattern Break | — | Jumps to row `xx` of the next pattern in the Order List. <br /> If the current pattern is the last pattern in the Order List, `Cxx` will jump to row `xx` of the first pattern. <br />If <abbr title="Position Jump">`Bxx`</abbr> is on the same row, the pattern specified by `Bxx` will be the pattern `Cxx` jumps in. <br /> Ranges from 00h to 3Fh (64; maximum amount of pattern rows for the S3M format); higher values are ignored. | Global *(Pattern)* |
| **`Dxy`** | Volume Slide <br /> *or* Fine Volume Slide | Global | Slides the current note volume up or down. <br /> * **`D0y` decreases** note volume by `y` units on every tick of the row except the first. <br /> If `y` is Fh and **Fast Volume Slides** are enabled, volume decreases on every tick. <br /> * **`Dx0` increases** note volume by `x` units on every tick of the row except the first. <br /> If `x` is Fh and **Fast Volume Slides** are enabled, volume decreases on every tick.<br /> Volume will not exceed 64 (40h). <br /> * **`DFy` finely decreases** note volume by only applying `y` units on the first tick of the row. <br /> `y` cannot be Fh. <br /> * **`DxF` finely increases** note volume by only applying `x` units on the first tick of the row. <br /> `x` cannot be Fh. | Volume |
| **`Exx`** | Portamento Down <br /> *or* Fine Portamento Down <br /> *or* Extra Fine Portamento Down | Global | Decreases current note pitch by `xx` **units** on every tick of the row except the first. <br /> * **`EFx` finely** decreases note pitch by only applying `x` **units** on the first tick of the row. <br /> * **`EEx` extra-finely** decreases note pitch by applying with 4 times the precision of `EFx`. | Pitch |
| **`Fxx`** | Portamento Up <br /> *or* Fine Portamento Up <br /> *or* Extra Fine Portamento Up | Global | Increases current note pitch by `xx` **units** on every tick of the row except the first. <br /> * **`FFx` finely** increases note pitch by only applying `x` **units** on the first tick of the row. <br /> * **`FEx` extra-finely** increases note pitch by applying with 4 times the precision of `EFx`. | Pitch |
| **`Gxx`** | Tone Portamento | Yes | Slides the pitch of the previous note towards the current note by `xx` **units** on every tick of the row except the first. | Pitch |
| **`Hxy`** | Vibrato | Yes | Executes vibrato with speed `x` and depth `y` on the current note. <br /> Modulates with selected vibrato waveform (see the **Waveform Types table** for more details). <br /> Shares memory with <abbr title="Fine Vibrato">`Uxy`</abbr>. | Pitch |
| **`Ixy`** | Tremor | Global | Rapidly switches the sample volume on and off. <br /> Volume is on for `x` ticks and off for `y` ticks. | Volume |
| **`Jxy`** | Arpeggio | Global | Plays an arpeggiation of three notes in one row, cycling between the current note, current note + `x` semitones, and current note + `y` semitones. | Pitch |
| **`Kxy`** | Volume Slide + Vibrato | Global | Functions like <abbr title="Volume Slide">`Dxy`</abbr> with <abbr title="Vibrato memory">`H00`</abbr>. <br /> Parameters are used like `Dxy`. | Miscellaneous |
| **`Lxy`** | Volume Slide + Tone Portamento | Global | Functions like <abbr title="Volume Slide">`Dxy`</abbr> with <abbr title="Tone Portamento memory">`G00`</abbr>. <br /> Parameters are used like `Dxy`. | Miscellaneous |
| **`Mxx`** | Set Channel Volume | — | > **Warning:** This is not an original Scream Tracker 3 effect. <br /> Sets the current channel volume, which multiplies all note volumes it encompasses. <br /> Ranges from 00h (off) to 40h (full). | Volume |
| **`Nxy`** | Channel Volume Slide | Yes | > **Warning:** This is not an original Scream Tracker 3 effect. <br /> Similar to <abbr title="Volume Slide">`Dxy`</abbr>, but applies to the current channel's volume. | Volume |
| **`Oxx`** | Sample Offset | Yes | Starts playing the current sample from position `xx` × 256, instead of position 0. <br /> Ineffective if there is no note in the same pattern cell. | Miscellaneous |
| **`Pxy`** | Panning Slide <br /> *or* Fine Panning Slide | Yes | > **Warning:** This is not an original Scream Tracker 3 effect. <br /> Slides the current channel's panning position left or right. <br /> * **`P0y`** slides the panning to the **right** by `y` units on every tick of the row except the first. <br /> * **`Px0`** slides the panning to the **left** by `x` units on every tick of the row except the first. <br /> * **`PFy` finely** slides the panning to the right by only applying `y` units on the first tick of the row. <br /> `y` cannot be Fh. <br /> * **`PxF` finely** slides the panning to the left by only applying `x` units on the first tick of the row. <br /> `x` cannot be Fh. | Panning |
| **`Qxy`** | Retrigger | Global | Retriggers the current note every `y` ticks and changes the volume based on the `x` value (see the **Retrigger Volume table** for more details). | Miscellaneous |
| **`Rxy`** | Tremolo | Global | Executes tremolo with speed `x` and depth `y` on the current note. <br /> Modulates with selected tremolo waveform (see the **Waveform Types table** for more details). | Volume |
| **`S1x`** | Glissando Control | <abbr title="Global memory can be called with S00">—</abbr> | > **Warning:** This effect is not widely supported and behaves quirky in OpenMPT. <br /> Configures whether tone portamento effects slide by semitones or not. <br /> * **`S10` disables** glissando. <br /> * **`S11` enables** glissando. | Pitch |
| **`S2x`** | Set Finetune | <abbr title="Global memory can be called with S00">—</abbr> | *Considered a legacy command.* <br /> Overrides the current sample's C-5 frequency with a MOD finetune value. | Pitch |
| **`S3x`** | Set Vibrato Waveform | <abbr title="Global memory can be called with S00">—</abbr> | Sets the waveform of future Vibrato effects (see the **Waveform Types table** for more details). | Pitch |
| **`S4x`** | Set Tremolo Waveform | <abbr title="Global memory can be called with S00">—</abbr> | Sets the waveform of future Tremolo effects (see the **Waveform Types table** for more details). | Volume |
| **`S5x`** | Set Panbrello Waveform | <abbr title="Global memory can be called with S00">—</abbr> | > **Warning:** This is not an original Scream Tracker 3 effect. <br /> Sets the waveform of future Panbrello effects (see the **Waveform Types table** for more details). | Panning |
| **`S6x`** | Fine Pattern Delay | <abbr title="Global memory can be called with S00">—</abbr> | Extends the current row by `x` ticks. <br /> If multiple `S6x` commands are on the same row, the sum of their parameters is used. | Global *(Pattern)* |
| **`S8x`** | Set Panning | <abbr title="Global memory can be called with S00">—</abbr> | *<abbr title="Set Panning">`Xxx`</abbr> is a much finer panning effect.* <br /> Sets the current channel's panning position. <br /> Ranges from 0h (left) to Fh (right). | Panning |
| **`S9x`** | Sound Control | <abbr title="Global memory can be called with S00">—</abbr> | > **Warning:** This is not an original Scream Tracker 3 effect. <br /> Executes a sound control command (see the **Sound Control table** for more details). | Miscellaneous |
| **`SAx`** | High Offset | <abbr title="Global memory can be called with S00">—</abbr> | > **Warning:** This is not an original Scream Tracker 3 effect. <br /> Sets the high offset for future <abbr title="Sample Offset">`Oxx`</abbr> commands. <br /> `x` × 65536 (10000h) is added to all offset effects that follow this command. | Miscellaneous |
| **`SB0`** | Pattern Loop Start | <abbr title="Global memory can be called with S00">—</abbr> | Marks the current row position to be used as the start of a pattern loop. | Global *(Pattern)* |
| **`SBx`** | Pattern Loop | <abbr title="Global memory can be called with S00">—</abbr> | Each time this command is reached, jumps to the row marked by <abbr title="Pattern Loop Start">`SB0`</abbr> until `x` jumps have occurred in total. <br /> If `SBx` is used in a pattern with no `SB0` effect, `SBx` will use the row position marked by any previous `SB0` effect. <br /> Pattern loops cannot span multiple patterns. <br /> Ranges from 1h to Fh. | Global *(Pattern)* |
| **`SCx`** | Note Cut | <abbr title="Global memory can be called with S00">—</abbr> | Stops the current sample after `x` ticks. <br /> If `x` is 0, or greater than or equal to the current module Speed, this command is ignored. | Miscellaneous |
| **`SDx`** | Note Delay | <abbr title="Global memory can be called with S00">—</abbr> | Delays the note or instrument change in the current pattern cell by `x` ticks. <br /> If `x` is 0, or greater than or equal to the current module Speed, the current pattern cell's contents are never played. | Miscellaneous |
| **`SEx`** | Pattern Delay | <abbr title="Global memory can be called with S00">—</abbr> | Repeats the current row `x` times. <br /> Notes are not retriggered on every repetition, but effects are still processed. <br /> If multiple `SEx` commands are on the same row, only the leftmost command is used. | Global *(Pattern)* |
| **`T0x`** | Decrease Tempo | Yes | Decreases the module Tempo by `x` BPM on every tick of the row except the first. | Global *(Timing)* |
| **`T1x`** | Increase Tempo | Yes | Increases the module Tempo by `x` BPM on every tick of the row except the first. | Global *(Timing)* |
| **`Txx`** | Set Tempo | No | Sets the module Tempo if `xx` is greater than or equal to 20h. | Global *(Timing)* |
| **`Uxy`** | Fine Vibrato | Yes | Similar to <abbr title="Vibrato">`Hxy`</abbr>, but with 4 times the precision. <br /> Shares memory with `Hxy`. | Pitch |
| **`Vxx`** | Set Global Volume | — | Sets the global volume. <br /> Ranges from 00h (off) to 40h (full). | Volume |
| **`Wxy`** | Global Volume Slide | Yes | > **Warning:** This is not an original Scream Tracker 3 effect. <br /> Similar to <abbr title="Volume Slide">`Dxy`</abbr>, but applies to the global volume. | Volume |
| **`Xxx`** | Set Panning | — | Sets the current channel's panning position. <br /> * **`XA4` enables surround playback** on the current channel. <br /> When using **stereo** playback, the right channel of a sample is played with inversed phase (Pro Logic Surround). <br /> When using **quad** playback, the rear channels are used for playing this channel. <br /> Surround mode can be disabled by using a different `Xxx` command on the same channel it was enabled on. <br /> Ranges from 00h (left) to 80h (right). | Panning |
| **`Yxy`** | Panbrello | Yes | > **Warning:** This is not an original Scream Tracker 3 effect. <br /> Executes panbrello with speed `x` and depth `y` on the current note. <br /> Modulates with selected panbrello waveform (see the **Waveform Types table** for more details). | Panning |
| **`Zxx`** | MIDI Macro | — | > **Warning:** This is not an original Scream Tracker 3 effect. <br /> Executes a **MIDI Macro**. <br /> Since MIDI Macros are not stored in S3M files, only the default macro configuration can be used to control the resonant filter. | Miscellaneous |

### Volume Column

The following commands can be entered into the volume column.

All parameter values are **decimal**.

| <abbr title="Effect">Eff</abbr> | Name | <abbr title="Memory">Mem</abbr><sup>†</sup> | Description | Category |
|---|---|---|---|---|
| **`pxx`** | Set Panning | No | > **Warning:** This is not an original Scream Tracker 3 effect. <br /> Sets the current channel's panning position to `xx`. <br /> Ranges from 0 to 64 (decimal). | Panning |
| **`vxx`** | Set Volume | No | Sets the current note volume to `xx`. <br /> Ranges from 0 to 64 (decimal). | Volume |

**<sup>†</sup> Effect Memory:** <br />
Assuming that all available parameters for a given effect (e.g. `xx`, `xy`, `x`, or `y`) are equivalent to 0:
* **No** means that the command does nothing.
* **Yes** means that the effect calls its own parameter memory. <br /> For example, if the effect <abbr title="Vibrato with speed 8 and depth 2">`H82`</abbr> is followed by the effect <abbr title="Vibrato memory">`H00`</abbr> on a subsequent row, the `H00` command recalls the effect parameter 82h.
* **—** means that the value has no special meaning. <br /> For example, <abbr title="Set Global Volume">`V00`</abbr> sets the global volume to 0, <abbr title="Set Panning">`X00`</abbr> sets the channel's panning position to hard left, <abbr title="Position Jump">`B00`</abbr> jumps to the first pattern, etc.
* **Global** means that the effect calls any previous non-zero parameter in the same column. <br /> For example, if the effect <abbr title="Portamento Down by 22h (34) units">`E22`</abbr> is followed by <abbr title="Tremor memory">`I00`</abbr>, the `I00` command recalls the effect parameter 22h.


### AdLib / OPL3 Instruments
OpenMPT supports AdLib / OPL3 instruments in the S3M format, but not all effects are compatible with them. Below is a list of effects that do not function with AdLib / OPL3 instruments in S3M files:
* `Oxx` (Sample Offset)
* `S9x` (Sound Control)
* `SAx` (High Offset)
* `XA4` (Enable Surround)

The following effects are not supported in Scream Tracker 3 but work with AdLib / OPL3 instruments in OpenMPT:
* `Mxx` (Set Channel Volume)
* `Nxy` (Channel Volume Slide)

Additionally, since AdLib / OPL3 instruments only support hard left, center, and hard right panning, the range of <abbr title="Set Panning">`Xxx`</abbr> and <abbr title="Set Panning">`pxx`</abbr> are effectively restricted to those positions.

## IT Effect Commands

Impulse Tracker's IT format uses a command set that expands upon the S3M format's, adding several new effects and improving effect memory usage.

### Effect Column

The following commands can be entered into the effect column.

All parameter values are **hexadecimal**.

| <abbr title="Effect">Eff</abbr> | Name | <abbr title="Memory">Mem</abbr><sup>†</sup> | Description | Category |
|---|---|---|---|---|
| **`Axx`** | Set Speed | No | Sets the module Speed (ticks per row). | Global *(Timing)* |
| **`Bxx`** | Position Jump | — | Causes playback to jump to pattern position `xx`. <br /> `B00` would restart a song from the beginning (first pattern in the Order List). <br /> If <abbr title="Pattern Break">`Cxx`</abbr> is on the same row, the pattern specified by `Bxx` will be the pattern `Cxx` jumps in. | Global *(Pattern)* |
| **`Cxx`** | Pattern Break | — | Jumps to row `xx` of the next pattern in the Order List. <br /> If the current pattern is the last pattern in the Order List, `Cxx` will jump to row `xx` of the first pattern. <br /> If <abbr title="Position Jump">`Bxx`</abbr> is on the same row, the pattern specified by `Bxx` will be the pattern `Cxx` jumps in. <br /> Ranges from 00h to the next pattern's row length; higher values are treated as 00h. | Global *(Pattern)* |
| **`Dxy`** | Volume Slide <br /> *or* Fine Volume Slide | Yes | Slides the current note volume up or down. <br /> * **`D0y` decreases** note volume by `y` units on every tick of the row except the first. <br /> If `y` is Fh, volume decreases on every tick. <br /> * **`Dx0` increases** note volume by `x` units on every tick of the row except the first. <br /> Volume will not exceed 64 (40h). <br /> * **`DFy` finely decreases** note volume by only applying `y` units on the first tick of the row. <br /> `y` cannot be Fh. <br /> * **`DxF` finely increases** note volume by only applying `x` units on the first tick of the row. <br /> `x` cannot be Fh. | Volume |
| **`Exx`** | Portamento Down <br /> *or* Fine Portamento Down <br /> *or* Extra Fine Portamento Down | Yes | Decreases current note pitch by `xx` **units** on every tick of the row except the first. <br /> * **`EFx` finely** decreases note pitch by only applying `x` **units** on the first tick of the row. <br /> * **`EEx` extra-finely** decreases note pitch by applying with 4 times the precision of `EFx`. | Pitch |
| **`Fxx`** | Portamento Up <br /> *or* Fine Portamento Up <br /> *or* Extra Fine Portamento Up | Yes | Increases current note pitch by `xx` **units** on every tick of the row except the first. <br /> * **`FFx` finely** increases note pitch by only applying `x` **units** on the first tick of the row. <br /> * **`FEx` extra-finely** increases note pitch by applying with 4 times the precision of `EFx`. | Pitch |
| **`Gxx`** | Tone Portamento | Yes | Slides the pitch of the previous note towards the current note by `xx` **units** on every tick of the row except the first. | Pitch |
| **`Hxy`** | Vibrato | Yes | Executes vibrato with speed `x` and depth `y` on the current note. <br /> Modulates with selected vibrato waveform (see the **Waveform Types table** for more details). <br /> Shares memory with <abbr title="Fine Vibrato">`Uxy`</abbr>. | Pitch |
| **`Ixy`** | Tremor | Yes | Rapidly switches the sample volume on and off. <br /> Volume is on for `x` ticks and off for `y` ticks. <br /> For instrument plugins > **Warning:** (ModPlug hack), this command sends note-on and note-off messages instead of modifying the volume. | Volume |
| **`Jxy`** | Arpeggio | Yes | Plays an arpeggiation of three notes in one row, cycling between the current note, current note + `x` semitones, and current note + `y` semitones. | Pitch |
| **`Kxy`** | Volume Slide + Vibrato | Yes | Functions like <abbr title="Volume Slide">`Dxy`</abbr> with <abbr title="Vibrato memory">`H00`</abbr>. <br /> Parameters are used like `Dxy`. | Miscellaneous |
| **`Lxy`** | Volume Slide + Tone Portamento | Yes | Functions like <abbr title="Volume Slide">`Dxy`</abbr> with <abbr title="Tone Portamento memory">`G00`</abbr>. <br /> Parameters are used like `Dxy`. | Miscellaneous |
| **`Mxx`** | Set Channel Volume | — | Sets the current channel volume, which multiplies all note volumes it encompasses. <br /> Ranges from 00h (off) to 40h (full). | Volume |
| **`Nxy`** | Channel Volume Slide | Yes | Similar to <abbr title="Volume Slide">`Dxy`</abbr>, but applies to the current channel's volume. | Volume |
| **`Oxx`** | Sample Offset | Yes | Starts playing the current sample from position `xx` × 256, instead of position 0. <br /> Ineffective if there is no note in the same pattern cell. | Miscellaneous |
| **`Pxy`** | Panning Slide <br /> *or* Fine Panning Slide | Yes | Slides the current channel's panning position left or right. <br /> * **`P0y`** slides the panning to the **right** by `y` units on every tick of the row except the first. <br /> * **`Px0`** slides the panning to the **left** by `x` units on every tick of the row except the first. <br /> * **`PFy` finely** slides the panning to the right by only applying `y` units on the first tick of the row. <br /> `y` cannot be Fh. <br /> * **`PxF` finely** slides the panning to the left by only applying `x` units on the first tick of the row. <br /> `x` cannot be Fh. | Panning |
| **`Qxy`** | Retrigger | Yes | Retriggers the current note every `y` ticks and changes the volume based on the `x` value (see the **Retrigger Volume table** for more details). | Miscellaneous |
| **`Rxy`** | Tremolo | Yes | Executes tremolo with speed `x` and depth `y` on the current note. <br /> Modulates with selected tremolo waveform (see the **Waveform Types table** for more details). | Volume |
| **`S1x`** | Glissando Control | <abbr title="Memory can be called with S00">—</abbr> | > **Warning:** This effect is not widely supported and behaves quirky in OpenMPT. <br /> Configures whether tone portamento effects slide by semitones or not. <br /> * **`S10` disables** glissando. <br /> * **`S11` enables** glissando. | Pitch |
| **`S2x`** | Set Finetune | <abbr title="Memory can be called with S00">—</abbr> | *Considered a legacy command.* <br /> Overrides the current sample's C-5 frequency with a MOD finetune value. | Pitch |
| **`S3x`** | Set Vibrato Waveform | <abbr title="Memory can be called with S00">—</abbr> | Sets the waveform of future Vibrato effects (see the **Waveform Types table** for more details). | Pitch |
| **`S4x`** | Set Tremolo Waveform | <abbr title="Memory can be called with S00">—</abbr> | Sets the waveform of future Tremolo effects (see the **Waveform Types table** for more details). | Volume |
| **`S5x`** | Set Panbrello Waveform | <abbr title="Memory can be called with S00">—</abbr> | Sets the waveform of future Panbrello effects (see the **Waveform Types table** for more details). | Panning |
| **`S6x`** | Fine Pattern Delay | <abbr title="Memory can be called with S00">—</abbr> | Extends the current row by `x` ticks. <br /> If multiple `S6x` commands are on the same row, the sum of their parameters is used. | Global *(Pattern)* |
| **`S70`** | Past Note Cut | <abbr title="Memory can be called with S00">—</abbr> | **Cuts** all notes playing as a result of New Note Actions on the current channel. | Miscellaneous |
| **`S71`** | Past Note Off | <abbr title="Memory can be called with S00">—</abbr> | **Sends a Note Off** to all notes playing as a result of New Note Actions on the current channel. | Miscellaneous |
| **`S72`** | Past Note Fade | <abbr title="Memory can be called with S00">—</abbr> | **Fades out** all notes playing as a result of New Note Actions on the current channel. | Miscellaneous |
| **`S73`** | NNA Note Cut | <abbr title="Memory can be called with S00">—</abbr> | Sets the currently active note's New Note Action to **Note Cut**. | Miscellaneous |
| **`S74`** | NNA Note Continue | <abbr title="Memory can be called with S00">—</abbr> | Sets the currently active note's New Note Action to **Continue**. | Miscellaneous |
| **`S75`** | NNA Note Off | <abbr title="Memory can be called with S00">—</abbr> | Sets the currently active note's New Note Action to **Note Off**. | Miscellaneous |
| **`S76`** | NNA Note Fade | <abbr title="Memory can be called with S00">—</abbr> | Sets the currently active note's New Note Action to **Note Fade**. | Miscellaneous |
| **`S77`** | Volume Envelope Off | <abbr title="Memory can be called with S00">—</abbr> | **Disables** the currently active note's **volume** envelope. | Miscellaneous |
| **`S78`** | Volume Envelope On | <abbr title="Memory can be called with S00">—</abbr> | **Enables** the currently active note's **volume** envelope. | Miscellaneous |
| **`S79`** | Panning Envelope Off | <abbr title="Memory can be called with S00">—</abbr> | **Disables** the currently active note's **panning** envelope. | Miscellaneous |
| **`S7A`** | Panning Envelope On | <abbr title="Memory can be called with S00">—</abbr> | **Enables** the currently active note's **panning** envelope. | Miscellaneous |
| **`S7B`** | Pitch Envelope Off | <abbr title="Memory can be called with S00">—</abbr> | **Disables** the currently active note's **pitch** or **filter** envelope. | Miscellaneous |
| **`S7C`** | Pitch Envelope On | <abbr title="Memory can be called with S00">—</abbr> | **Enables** the currently active note's **pitch** envelope. | Miscellaneous |
| **`S8x`** | Set Panning | <abbr title="Memory can be called with S00">—</abbr> | *<abbr title="Set Panning">`Xxx`</abbr> is a much finer panning effect.* <br /> Sets the current channel's panning position. <br /> Ranges from 0h (left) to Fh (right). | Panning |
| **`S9x`** | Sound Control | <abbr title="Memory can be called with S00">—</abbr> | > **Warning:** Only <abbr title="Surround On">`S91`</abbr> is an original Impulse Tracker command. <br /> Executes a sound control command (see the **Sound Control table** for more details). | Miscellaneous |
| **`SAx`** | High Offset | <abbr title="Memory can be called with S00">—</abbr> | Sets the high offset for future <abbr title="Sample Offset">`Oxx`</abbr> commands. <br /> `x` × 65536 (10000h) is added to all offset effects that follow this command. | Miscellaneous |
| **`SB0`** | Pattern Loop Start | <abbr title="Memory can be called with S00">—</abbr> | Marks the current row position to be used as the start of a pattern loop. | Global *(Pattern)* |
| **`SBx`** | Pattern Loop | <abbr title="Memory can be called with S00">—</abbr> | Each time this command is reached, jumps to the row marked by <abbr title="Pattern Loop Start">`SB0`</abbr> until `x` jumps have occurred in total. <br /> If `SBx` is used in a pattern with no `SB0` effect, `SBx` will use the row position marked by any previous `SB0` effect. <br /> Pattern loops cannot span multiple patterns. <br /> Ranges from 1h to Fh. | Global *(Pattern)* |
| **`SCx`** | Note Cut | <abbr title="Memory can be called with S00">—</abbr> | Stops the current sample after `x` ticks. <br /> If `x` is greater than or equal to the current module Speed, this command is ignored. <br /> If `x` is 0, it will be treated as if it were 1. | Miscellaneous |
| **`SDx`** | Note Delay | <abbr title="Memory can be called with S00">—</abbr> | Delays the note or instrument change in the current pattern cell by `x` ticks. <br /> If `x` is greater than or equal to the current module Speed, the current cell's contents are not played. <br /> If `x` is 0, it will be treated as if it were 1. | Miscellaneous |
| **`SEx`** | Pattern Delay | <abbr title="Memory can be called with S00">—</abbr> | Repeats the current row `x` times. <br /> Notes are not retriggered on every repetition, but effects are still processed. <br /> If multiple `SEx` commands are on the same row, only the leftmost command is used. | Global *(Pattern)* |
| **`SFx`** | Set Active Macro | <abbr title="Memory can be called with S00">—</abbr> | Sets the current channel's active **parametered macro**. | Miscellaneous |
| **`T0x`** | Decrease Tempo | Yes | Decreases the module Tempo by `x` BPM on every tick of the row except the first. | Global *(Timing)* |
| **`T1x`** | Increase Tempo | Yes | Increases the module Tempo by `x` BPM on every tick of the row except the first. | Global *(Timing)* |
| **`Txx`** | Set Tempo | No | Sets the module Tempo if `xx` is greater than or equal to 20h. | Global *(Timing)* |
| **`Uxy`** | Fine Vibrato | Yes | Similar to <abbr title="Vibrato">`Hxy`</abbr>, but with 4 times the precision. <br /> Shares memory with `Hxy`. | Pitch |
| **`Vxx`** | Set Global Volume | — | Sets the global volume. <br /> Ranges from 00h (off) to 80h (full). | Volume |
| **`Wxy`** | Global Volume Slide | Yes | Similar to <abbr title="Volume Slide">`Dxy`</abbr>, but applies to the global volume. | Volume |
| **`Xxx`** | Set Panning | — | Sets the current channel's panning position. <br /> Ranges from 00h (left) to FFh (right). | Panning |
| **`Yxy`** | Panbrello | Yes | Executes panbrello with speed `x` and depth `y` on the current note. <br /> Modulates with selected panbrello waveform (see the **Waveform Types table** for more details). | Panning |
| **`Zxx`** | MIDI Macro | — | Executes a **MIDI Macro**. | Miscellaneous |
| **`\xx`** | Smooth MIDI Macro | — | > **Warning:** This effect is a ModPlug hack. <br /> Executes an interpolated MIDI Macro. | Miscellaneous |

### Volume Column

The following commands can be entered into the volume column.

All parameter values are **decimal**.

| <abbr title="Effect">Eff</abbr> | Name | <abbr title="Memory">Mem</abbr><sup>†</sup> | Description | Category |
|---|---|---|---|---|
| **`a0x`** | Fine Volume Slide Up | Yes | Functions like <abbr title="Fine Volume Slide Up">`DxF`</abbr> (slides the volume up `x` units on the first tick). | Volume |
| **`b0x`** | Fine Volume Slide Down | Yes | Functions like <abbr title="Fine Volume Slide Down">`DFy`</abbr> (slides the volume down `x` units on the first tick). | Volume |
| **`c0x`** | Volume Slide Up | Yes | Functions like <abbr title="Volume Slide Up">`Dx0`</abbr> (slides the volume up `x` units on all ticks except the first). | Volume |
| **`d0x`** | Volume Slide Down | Yes | Functions like <abbr title="Volume Slide Down">`D0y`</abbr> (slides the volume down `x` units on all ticks except the first). | Volume |
| **`e0x`** | Portamento Down | Yes | Similar to <abbr title="Portamento Down">`Exx`</abbr>. <br /> Compared to `Exx`, parameters are 4 times more coarse (e.g. `e01` = <abbr title="Portamento Down by 4 units">`E04`</abbr>). | Pitch |
| **`f0x`** | Portamento Up | Yes | Similar to <abbr title="Portamento Up">`Fxx`</abbr>. <br /> Compared to `Fxx`, parameters are 4 times more coarse (e.g. `f01` = <abbr title="Portamento Up by 4 units">`F04`</abbr>). | Pitch |
| **`g0x`** | Tone Portamento | Yes | Similar to <abbr title="Tone Portamento">`Gxx`</abbr>. <br /> Below is a table that translates `g0x` parameters to `Gxx` parameters: <table><tr><th>g0x</th><th>Gxx</th></tr><tr><td><code>g00</code></td><td><code>G00</code></td></tr><tr><td><code>g01</code></td><td><code>G01</code></td></tr><tr><td><code>g02</code></td><td><code>G04</code></td></tr><tr><td><code>g03</code></td><td><code>G08</code></td></tr><tr><td><code>g04</code></td><td><code>G10</code></td></tr><tr><td><code>g05</code></td><td><code>G20</code></td></tr><tr><td><code>g06</code></td><td><code>G40</code></td></tr><tr><td><code>g07</code></td><td><code>G60</code></td></tr><tr><td><code>g08</code></td><td><code>G80</code></td></tr><tr><td><code>g09</code></td><td><code>GFF</code></td></tr></table> | Pitch |
| **`h0x`** | Vibrato Depth | Yes | Executes a vibrato with depth `x` and speed from the last <abbr title="Vibrato">`Hxy`</abbr> or <abbr title="Fine Vibrato">`Uxy`</abbr> command. | Pitch |
| **`pxx`** | Set Panning | — | Sets the current channel's panning position to `xx`. <br /> Ranges from 0 to 64. | Panning |
| **`vxx`** | Set Volume | — | Sets the current note volume to `xx`. <br /> The behaviour of this command when sent to instrument plugins can be configured in the **Instrument Editor** > **Warning:** (ModPlug hack). | Volume |

**<sup>†</sup> Effect Memory:** <br />
Assuming that all available parameters for a given effect (e.g. `xx`, `xy`, `x`, or `y`) are equivalent to 0:
* **No** means that the command does nothing.
* **Yes** means that the effect calls its own parameter memory. <br /> For example, if the effect <abbr title="Vibrato with speed 8 and depth 2">`H82`</abbr> is followed by the effect <abbr title="Vibrato memory">`H00`</abbr> on a subsequent row, the `H00` command recalls the effect parameter 82h.
* **—** means that the value has no special meaning. <br /> For example, <abbr title="Set Global Volume">`V00`</abbr> sets the global volume to 0, <abbr title="Set Panning">`X00`</abbr> sets the channel's panning position to hard left, <abbr title="Position Jump">`B00`</abbr> jumps to the first pattern, etc.

* **Volume Column Effect Memory:**
  * All volume slide effects in the volume column share effect memory with each other.
  * <abbr title="Portamento Down">`e0x`</abbr> and <abbr title="Portamento Up">`f0x`</abbr> share effect memory with <abbr title="Portamento Down">`Exx`</abbr> and <abbr title="Portamento Up">`Fxx`</abbr> (respectively).

## MPTM Effect Commands

OpenMPT′s MPTM format is heavily based on the IT format and **its effect command set**, but it does add several new features.

### Effect Column

OpenMPT's MPTM format generally uses the same effect commands as the IT format. Commands from the IT section that are labeled as ModPlug hacks or unsupported by Impulse Tracker may be freely used in the MPTM format.

The following additional commands, exclusive to the MPTM format, can be entered in the effect column.

All parameter values are **hexadecimal**.

| <abbr title="Effect">Eff</abbr> | Name | <abbr title="Memory">Mem</abbr><sup>†</sup> | Description | Category |
|---|---|---|---|---|
| **`S7D`** | Force Pitch Envelope | <abbr title="Memory can be called with S00">—</abbr> | Enables the currently active note's pitch envelope and forces it to act as a pitch envelope (rather than a filter cutoff envelope). | Miscellaneous |
| **`S7E`** | Force Filter Envelope | <abbr title="Memory can be called with S00">—</abbr> | Enables the currently active note's pitch envelope and forces it to act as a filter cutoff envelope (rather than a pitch envelope). | Miscellaneous |
| **`:xy`** | Note Delay + Cut | — | Delays the note in the current pattern cell by `x` ticks and cuts it after `x` + `y` ticks. <br /> If `x` is greater than or equal to the current module Speed, the note will be ignored (and as such, cannot be cut with `y`). <br /> If `x` + `y` is greater than or equal to the current module Speed, only the note cut will be ignored. | Miscellaneous |
| **`#xx`** | Parameter Extension | — | Extends the parameter of the last <abbr title="Position Jump">`Bxx`</abbr>, <abbr title="Pattern Break">`Cxx`</abbr>, <abbr title="Sample Offset">`Oxx`</abbr>, <abbr title="Set Tempo">`Txx`</abbr>, <abbr title="Finetune">`+xx`</abbr> or <abbr title="Finetune (Smooth)">`*xx`</abbr> command. If placed after such a command, the parameter values are combined. <br /> If there is only one `#xx` command below the actual command (the limit for `Bxx`, `Cxx`, and `Txx`), the original command is multiplied by 256 and then parameter `xx` is added to it. <br /> For `Oxx`, up to 4 rows can be combined. The fourth row is multiplied by 1, the third by 256 (100h), the second by 65536 (10000h), and so on. <br /><br /> **Example:** <br /> ``` <br />ModPlug Tracker MPT <br />|C-501...O21 <br />|........#02 <br />|........#01 <br />``` <br />In this example, the **hexadecimal** sample offset is (21h × 10000h) + (2h × 100h) + 1h = 210201h. <br /> In **decimal**, it is (33 × 65536) + (2 × 256) + 1 = 2,163,201. | Miscellaneous |
| **`+xx`** | Finetune | — | Changes the current note’s tuning. `+80` is center (no change to the tuning), lower values decrease the tuning and higher values increase the tuning. In sample mode, the depth of this command is fixed at ±1 semitone. For sample-based instruments, the depth is defined by the instrument’s **pitch bend range** setting. For plugins, this command directly translates to MIDI pitch bend commands, so the depth is whatever the pitch bend depth of the plugin or MIDI device is set to.<br>This command can be extended by `#xx` Parameter extensions for even finer control over the tuning. | Pitch |
| **`*xx`** | Finetune (Smooth) | — | Exactly the same as <abbr title="Finetune">`+xx`</abbr>, but slides from the previous finetune value to the new value on every tick of the row. | Pitch |

### Volume Column

In addition to all of the IT format's **volume column commands**, the following commands can be entered in the volume column.

All parameter values are **decimal**.

| <abbr title="Effect">Eff</abbr> | Name | <abbr title="Memory">Mem</abbr><sup>†</sup> | Description | Category |
|---|---|---|---|---|
| **`o0x`** | Sample Cue | Yes | Starts playing the current sample from cue point `x`, instead of position 0. <br /> Cue points can be chosen in the **Sample Editor**. <br /> Ineffective if there is no note in the same pattern cell. <br /> Shares effect memory with <abbr title="Sample Offset">`Oxx`</abbr> and can be **combined** with that command. | Miscellaneous |

**<sup>†</sup> Effect Memory:** <br />
Assuming that all available parameters for a given effect (e.g. `xx`, `xy`, `x`, or `y`) are equivalent to 0:
* **Yes** means that the effect calls its own parameter memory. <br /> For example, if the effect <abbr title="Vibrato with speed 8 and depth 2">`H82`</abbr> is followed by the effect <abbr title="Vibrato memory">`H00`</abbr> on a subsequent row, the `H00` command recalls the effect parameter 82h.
* **—** means that the value has no special meaning. <br /> For example, <abbr title="Set Global Volume">`V00`</abbr> sets the global volume to 0, <abbr title="Set Panning">`X00`</abbr> sets the channel's panning position to hard left, <abbr title="Position Jump">`B00`</abbr> jumps to the first pattern, etc.

### Combined Commands

By combining the volume column <abbr title="Sample Cue">`oxx`</abbr> and effect column <abbr title="Sample Offset">`Oxx`</abbr> commands, two special offset behaviours can be achieved:
* `o00` + `Oxx`: A cue point command with a parameter of 0 turns the effect column offset into a percentage offset, i.e. the command jumps to `xx` × <sup>1</sup>/<sub>256</sub>th of the total sample length. For example `o00` + `O80` plays the second half of any sample. Further precision can be achieved by combining the `Oxx` command with `#xx`.
* `o0x` + `Oyy`: For `x` > 0, the effect column `Oyy` is added on top of the cue point, so `o05` + `O01` plays from the 5th cue point of the sample + 256 samples.

### AdLib / OPL3 Instruments
OpenMPT supports AdLib / OPL3 instruments in the MPTM format, but not all effects are compatible with them. Below is a list of effects that do not function with AdLib / OPL3 instruments in MPTM files:
* `Oxx` (Sample Offset)
* `S9x` (Sound Control) parameters other than Ch and Dh (Global & Local Filters)
* `SAx` (High Offset)
* `o0x` (Sample Cue)

Additionally, since AdLib / OPL3 instruments only support hard left, center, and hard right panning, the range of <abbr title="Set Panning">`Xxx`</abbr> and <abbr title="Set Panning">`pxx`</abbr> are effectively restricted to those positions. Panning Slides like <abbr title="Panning Slide">`Pxy`</abbr> and <abbr title="Panbrello">`Yxy`</abbr> work with the same reduced granularity, but should be best avoided if possible.

### Parameter Control Events

Another feature similar to effects that is only available in MPTM files are **Parameter Control Events**, which can be used to automate plugin parameters.

