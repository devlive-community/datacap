import {ReleaseNote} from "@/views/common/release/ReleaseNote";
import Release20221115 from "@/views/common/release/Release20221115";
import Release20221015 from "@/views/common/release/Release20221015";
import Release120 from "@/views/common/release/Release120";
import Release130 from "@/views/common/release/Release130";
import Release140 from "@/views/common/release/Release140";

const releaseNotes: ReleaseNote[] = [];
releaseNotes.push(Release140.releaseNote,
  Release130.releaseNote,
  Release120.releaseNote,
  Release20221115.releaseNote,
  Release20221015.releaseNote);

export default {
  releaseNotes
}
