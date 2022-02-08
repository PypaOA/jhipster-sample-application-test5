import dayjs from 'dayjs/esm';
import { State } from 'app/entities/enumerations/state.model';

export interface ICd {
  id?: number;
  name?: string;
  performer?: string | null;
  releaseYear?: string | null;
  discCount?: string | null;
  medium?: string | null;
  label?: string | null;
  state?: State | null;
  added?: dayjs.Dayjs | null;
}

export class Cd implements ICd {
  constructor(
    public id?: number,
    public name?: string,
    public performer?: string | null,
    public releaseYear?: string | null,
    public discCount?: string | null,
    public medium?: string | null,
    public label?: string | null,
    public state?: State | null,
    public added?: dayjs.Dayjs | null
  ) {}
}

export function getCdIdentifier(cd: ICd): number | undefined {
  return cd.id;
}
