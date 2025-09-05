# Impulse Tracker (.it) Effect Commands

This document lists all effects for the Impulse Tracker (.it) format. Commands in the effect column use uppercase letters (e.g. `G05`), while commands in the volume column use lowercase letters (e.g. `g05`). The behavior of some effects is influenced by **Compatible Playback** settings.

## Effect Layout

An effect command consists of an effect letter (`A`) and a parameter (`xy`). The parameter is hexadecimal in the effect column and decimal in the volume column.
- `xx`: a 2-digit hexadecimal number.
- `xy`: two separate 1-digit hexadecimal numbers.

## Frequency Units

With **Linear Frequency Slides** enabled, a pitch slide unit (e.g., `Exx`, `Gxx`) is 1/16 of a semitone. Extra fine units (e.g., `EEx`, `FEx`) are 1/64 of a semitone.

Without linear slides, a pitch slide unit is one *period* (inverse to frequency). The effect of one unit is smaller for lower notes and larger for higher notes.

## Common Effect Parameters

### Waveform Types

For Vibrato, Tremolo, and Panbrello.

| Parameter | Waveform |
|---|---|
| 0 (default) | Sine (retrigger) |
| 1 | Sawtooth (retrigger) |
| 2 | Square (retrigger) |
| 3 | Random (retrigger) |

**Retrigger**: The oscillator waveform restarts when a new note is played. The waveform is 64 points long; speed determines points advanced per tick.

### Retrigger Volume

For `Qxy`, the note is retriggered every `y` ticks, and volume is changed based on `x`.

| Parameter | Effect | Parameter | Effect |
|---|---|---|---|
| 0 | No volume change | 8 | No volume change |
| 1 | Volume - 1 | 9 | Volume + 1 |
| 2 | Volume - 2 | A | Volume + 2 |
| 3 | Volume - 4 | B | Volume + 4 |
| 4 | Volume - 8 | C | Volume + 8 |
| 5 | Volume - 16 | D | Volume + 16 |
| 6 | Volume × ⅔ | E | Volume × 1.5 |
| 7 | Volume × ½ | F | Volume × 2 |

### Sound Control

For `S9x`. (Warning: Using these outside MPTM format, except `S91`, is a ModPlug hack).

| Parameter | Name | Description |
|---|---|---|
| 0 | Surround Off | Disables surround playback on the current channel. For Quad Surround Panning only. |
| 1 | Surround On | Enables surround playback. In stereo, right channel is phase-inversed (Pro Logic). In quad, uses rear channels. |
| 8 | Reverb Off | Disables Reverb on the current channel. |
| 9 | Reverb On | Enables Reverb on the current channel. (Warning: No per-song reverb config, use a plugin instead). |
| A | Center Surround | Sets surround mode to Center Surround for all channels (default). `S91` places channel in center rear; panning returns it to front. |
| B | Quad Surround | Sets surround mode to Quad Surround for all channels. Panning adjusts rear channels. `S91`/`S90` switch between front/rear. |
| C | Global Filters | Sets filter mode to Global (IT behavior). Resonant filters stay active until explicitly disabled (`Z7F` cutoff, `Z80` resonance). |
| D | Local Filters | Sets filter mode to Local. Resonant filter affects only the current note. |
| E | Play Forward | Forces the current sample to play forward. |
| F | Play Backward | Forces the current sample to play backward. |

## IT Effect Commands

### Effect Column

All parameter values are **hexadecimal**.

| Eff | Name | Mem† | Description | Category |
|---|---|---|---|---|
| `Axx` | Set Speed | No | Sets module Speed (ticks per row). | Global (Timing) |
| `Bxx` | Position Jump | — | Jumps to pattern position `xx`. `B00` restarts song. If `Cxx` is on same row, `Bxx` specifies pattern to jump to. | Global (Pattern) |
| `Cxx` | Pattern Break | — | Jumps to row `xx` of next pattern. If on last pattern, jumps to first. If `Bxx` is on same row, it specifies pattern to jump to. | Global (Pattern) |
| `Dxy` | Volume Slide / Fine Volume Slide | Yes | Slides note volume. `D0y` down, `Dx0` up. `DFy` fine down (first tick), `DxF` fine up (first tick). | Volume |
| `Exx` | Portamento Down / Fine / Extra Fine | Yes | Decreases pitch. `EFx` fine (first tick). `EEx` extra-fine (4x precision). | Pitch |
| `Fxx` | Portamento Up / Fine / Extra Fine | Yes | Increases pitch. `FFx` fine (first tick). `FEx` extra-fine (4x precision). | Pitch |
| `Gxx` | Tone Portamento | Yes | Slides pitch from previous note to current note. | Pitch |
| `Hxy` | Vibrato | Yes | Vibrato with speed `x` and depth `y`. Uses selected waveform. Shares memory with `Uxy`. | Pitch |
| `Ixy` | Tremor | Yes | Rapidly switches volume on (`x` ticks) and off (`y` ticks). (ModPlug hack: sends note-on/off for plugins). | Volume |
| `Jxy` | Arpeggio | Yes | Cycles between current note, note + `x` semitones, and note + `y` semitones. | Pitch |
| `Kxy` | Volume Slide + Vibrato | Yes | Combines `Dxy` and `H00` (vibrato with memory). | Miscellaneous |
| `Lxy` | Volume Slide + Tone Portamento | Yes | Combines `Dxy` and `G00` (portamento with memory). | Miscellaneous |
| `Mxx` | Set Channel Volume | — | Sets channel volume (00h-40h). | Volume |
| `Nxy` | Channel Volume Slide | Yes | Like `Dxy`, but for channel volume. | Volume |
| `Oxx` | Sample Offset | Yes | Starts sample from position `xx` × 256. No effect without a note. | Miscellaneous |
| `Pxy` | Panning Slide / Fine Panning Slide | Yes | Slides panning. `P0y` right, `Px0` left. `PFy` fine right (first tick), `PxF` fine left (first tick). | Panning |
| `Qxy` | Retrigger | Yes | Retriggers note every `y` ticks, with volume change from `x` (see Retrigger Volume table). | Miscellaneous |
| `Rxy` | Tremolo | Yes | Tremolo with speed `x` and depth `y`. Uses selected waveform. | Volume |
| `S1x` | Glissando Control | — | `S10` disables, `S11` enables glissando (semitone-based portamento). (Warning: Not widely supported). | Pitch |
| `S2x` | Set Finetune | — | Overrides sample's C-5 frequency with a MOD finetune value. (Legacy command). | Pitch |
| `S3x` | Set Vibrato Waveform | — | Sets waveform for future Vibrato effects. | Pitch |
| `S4x` | Set Tremolo Waveform | — | Sets waveform for future Tremolo effects. | Volume |
| `S5x` | Set Panbrello Waveform | — | Sets waveform for future Panbrello effects. | Panning |
| `S6x` | Fine Pattern Delay | — | Extends current row by `x` ticks. Summed if multiple on same row. | Global (Pattern) |
| `S70` | Past Note Cut | — | Cuts notes from New Note Actions on the channel. | Miscellaneous |
| `S71` | Past Note Off | — | Sends Note Off to notes from New Note Actions. | Miscellaneous |
| `S72` | Past Note Fade | — | Fades out notes from New Note Actions. | Miscellaneous |
| `S73` | NNA Note Cut | — | Sets active note's New Note Action to Note Cut. | Miscellaneous |
| `S74` | NNA Note Continue | — | Sets active note's New Note Action to Continue. | Miscellaneous |
| `S75` | NNA Note Off | — | Sets active note's New Note Action to Note Off. | Miscellaneous |
| `S76` | NNA Note Fade | — | Sets active note's New Note Action to Note Fade. | Miscellaneous |
| `S77` | Volume Envelope Off | — | Disables active note's volume envelope. | Miscellaneous |
| `S78` | Volume Envelope On | — | Enables active note's volume envelope. | Miscellaneous |
| `S79` | Panning Envelope Off | — | Disables active note's panning envelope. | Miscellaneous |
| `S7A` | Panning Envelope On | — | Enables active note's panning envelope. | Miscellaneous |
| `S7B` | Pitch Envelope Off | — | Disables active note's pitch/filter envelope. | Miscellaneous |
| `S7C` | Pitch Envelope On | — | Enables active note's pitch envelope. | Miscellaneous |
| `S8x` | Set Panning | — | Sets channel panning (0h-Fh). (`Xxx` is finer). | Panning |
| `S9x` | Sound Control | — | Executes a sound control command (see Sound Control table). (Warning: Only `S91` is an original IT command). | Miscellaneous |
| `SAx` | High Offset | — | Sets high offset for `Oxx` commands. Adds `x` × 65536 to subsequent offsets. | Miscellaneous |
| `SB0` | Pattern Loop Start | — | Marks start of a pattern loop. | Global (Pattern) |
| `SBx` | Pattern Loop | — | Jumps to `SB0` `x` times. Does not span patterns. | Global (Pattern) |
| `SCx` | Note Cut | — | Stops sample after `x` ticks. Ignored if `x` >= speed. `x=0` is treated as 1. | Miscellaneous |
| `SDx` | Note Delay | — | Delays note/instrument by `x` ticks. Not played if `x` >= speed. `x=0` is treated as 1. | Miscellaneous |
| `SEx` | Pattern Delay | — | Repeats current row `x` times. Notes are not retriggered, effects are processed. | Global (Pattern) |
| `SFx` | Set Active Macro | — | Sets the channel's active parametered macro. | Miscellaneous |
| `T0x` | Decrease Tempo | Yes | Decreases tempo by `x` BPM per tick (except first). | Global (Timing) |
| `T1x` | Increase Tempo | Yes | Increases tempo by `x` BPM per tick (except first). | Global (Timing) |
| `Txx` | Set Tempo | No | Sets tempo if `xx` >= 20h. | Global (Timing) |
| `Uxy` | Fine Vibrato | Yes | Like `Hxy`, but with 4x precision. Shares memory with `Hxy`. | Pitch |
| `Vxx` | Set Global Volume | — | Sets global volume (00h-80h). | Volume |
| `Wxy` | Global Volume Slide | Yes | Like `Dxy`, but for global volume. | Volume |
| `Xxx` | Set Panning | — | Sets channel panning (00h-FFh). | Panning |
| `Yxy` | Panbrello | Yes | Panbrello with speed `x` and depth `y`. Uses selected waveform. | Panning |
| `Zxx` | MIDI Macro | — | Executes a MIDI Macro. | Miscellaneous |
| `\xx` | Smooth MIDI Macro | — | Executes an interpolated MIDI Macro. (ModPlug hack). | Miscellaneous |

### Volume Column

All parameter values are **decimal**.

| Eff | Name | Mem† | Description | Category |
|---|---|---|---|---|
| `a0x` | Fine Volume Slide Up | Yes | Like `DxF` (slides volume up `x` units on first tick). | Volume |
| `b0x` | Fine Volume Slide Down | Yes | Like `DFy` (slides volume down `x` units on first tick). | Volume |
| `c0x` | Volume Slide Up | Yes | Like `Dx0` (slides volume up `x` units on all ticks except first). | Volume |
| `d0x` | Volume Slide Down | Yes | Like `D0y` (slides volume down `x` units on all ticks except first). | Volume |
| `e0x` | Portamento Down | Yes | Like `Exx`, but 4x coarser (e.g., `e01` = `E04`). | Pitch |
| `f0x` | Portamento Up | Yes | Like `Fxx`, but 4x coarser (e.g., `f01` = `F04`). | Pitch |
| `g0x` | Tone Portamento | Yes | Like `Gxx`. `g00`=`G00`, `g01`=`G01`, `g02`=`G04`, `g03`=`G08`, `g04`=`G10`, `g05`=`G20`, `g06`=`G40`, `g07`=`G60`, `g08`=`G80`, `g09`=`GFF`. | Pitch |
| `h0x` | Vibrato Depth | Yes | Vibrato with depth `x` and speed from last `Hxy`/`Uxy`. | Pitch |
| `pxx` | Set Panning | — | Sets channel panning (0-64). | Panning |
| `vxx` | Set Volume | — | Sets note volume (0-64). Behavior for plugins is configurable (ModPlug hack). | Volume |

**† Effect Memory:** When an effect's parameters are 0:
* **No**: The command does nothing.
* **Yes**: The effect uses its parameter memory (e.g., `H00` recalls the last `Hxy` parameters).
* **—**: The value 0 has a specific meaning (e.g., `V00` sets volume to 0).

**Volume Column Effect Memory:**
* Volume slide effects share memory.
* `e0x`/`f0x` share memory with `Exx`/`Fxx` respectively.
