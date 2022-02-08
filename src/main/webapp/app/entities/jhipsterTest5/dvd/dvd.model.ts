import dayjs from 'dayjs/esm';
import { State } from 'app/entities/enumerations/state.model';

export interface IDvd {
  id?: number;
  name?: string;
  performer?: string | null;
  releaseYear?: string | null;
  discCount?: string | null;
  format?: string | null;
  lang?: string | null;
  state?: State | null;
  added?: dayjs.Dayjs | null;
}

export class Dvd implements IDvd {
  constructor(
    public id?: number,
    public name?: string,
    public performer?: string | null,
    public releaseYear?: string | null,
    public discCount?: string | null,
    public format?: string | null,
    public lang?: string | null,
    public state?: State | null,
    public added?: dayjs.Dayjs | null
  ) {}
}

export function getDvdIdentifier(dvd: IDvd): number | undefined {
  return dvd.id;
}
