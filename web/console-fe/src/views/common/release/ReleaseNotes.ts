import {ReleaseNote} from "@/views/common/release/ReleaseNote";
import Release20221115 from "@/views/common/release/Release20221115";
import Release20221015 from "@/views/common/release/Release20221015";

const releaseNotes: ReleaseNote[] = [];
releaseNotes.push(Release20221015.releaseNote,
  Release20221115.releaseNote);

export default {
  releaseNotes
}
