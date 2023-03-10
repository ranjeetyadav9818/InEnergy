// This could be useful for conditional expressions with maybe empty collections

import {Injectable, Pipe, PipeTransform} from '@angular/core';

@Injectable()
@Pipe({name: 'nullSafePipe'})
export class DefaultValueIfNullPipe implements PipeTransform {
  transform(value: any) {
    if (value != null) {
      return value;
    } else {
      return '';
    }
  }


}
